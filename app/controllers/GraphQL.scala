package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import graphql.schema
import play.api.Configuration
import play.api.libs.json.{JsObject, JsString, Json}
import play.api.mvc.{Action, Controller}
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.parser.{QueryParser, SyntaxError}
import views.Config

import scala.concurrent.Future
import scala.util.{Failure, Success}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import repo.ContentRepo
import sangria.marshalling.playJson._

class GraphQL @Inject() (system: ActorSystem, config: Configuration, repo: ContentRepo) extends Controller  {
  import system.dispatcher

  val defaultQuery =
    """query ConferenceDetails {
      |  conference(edition: Berlin2017) {
      |    name
      |    dateStart
      |
      |    tickets {
      |      name
      |      price
      |    }
      |
      |    schedule {
      |      startTime
      |      duration
      |      entryType
      |
      |      ... on Talk {
      |        title
      |        description
      |
      |        speakers {
      |          name
      |          company
      |          twitter
      |          github
      |        }
      |      }
      |    }
      |
      |    speakers {
      |      name
      |      company
      |      twitter
      |      github
      |    }
      |
      |    sponsors {
      |      name
      |      url
      |      description
      |    }
      |
      |    team {
      |      name
      |      description
      |      twitter
      |    }
      |  }
      |}""".stripMargin

  val conf = config.underlying.as[Config]("graphqlEurope")

  def graphiql = Action {
    Ok(views.html.graphiql(conf, defaultQuery))
  }

  def graphql = Action.async(parse.json) { request ⇒
    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]

    val variables = (request.body \ "variables").toOption.flatMap {
      case JsString(vars) ⇒ Some(parseVariables(vars))
      case obj: JsObject ⇒ Some(obj)
      case _ ⇒ None
    }

    executeQuery(query, variables, operation)
  }

  private def parseVariables(variables: String) =
    if (variables.trim == "" || variables.trim == "null") Json.obj() else Json.parse(variables).as[JsObject]

  private def executeQuery(query: String, variables: Option[JsObject], operation: Option[String]) =
    QueryParser.parse(query) match {
      case Success(queryAst) ⇒
        Executor.execute(schema.ConferenceSchema, queryAst, repo,
          operationName = operation,
          variables = variables getOrElse Json.obj(),
          maxQueryDepth = Some(10))
            .map(Ok(_))
            .recover {
              case error: QueryAnalysisError ⇒ BadRequest(error.resolveError)
              case error: ErrorWithResolver ⇒ InternalServerError(error.resolveError)
            }

      case Failure(error: SyntaxError) ⇒
        Future.successful(BadRequest(Json.obj(
          "syntaxError" → error.getMessage,
          "locations" → Json.arr(Json.obj(
            "line" → error.originalError.position.line,
            "column" → error.originalError.position.column)))))

      case Failure(error) ⇒
        throw error
    }
}
