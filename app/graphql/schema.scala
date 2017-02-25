package graphql

import repo.ContentRepo
import sangria.schema._
import customScalars._
import sangria.marshalling.ScalaInput
import views.{Conference, Edition}

object schema {
  val EditionArg = Argument("edition", OptionInputType(Edition.graphqlType), ScalaInput.scalaInput(Edition.Berlin2017))

  val QueryType = ObjectType("Query", fields[ContentRepo, Unit](
    Field("conferences", ListType(Conference.graphqlType),
      resolve = c ⇒ c.ctx.conferences.sortBy(_.year).reverse),
    Field("conference", OptionType(Conference.graphqlType),
      arguments = EditionArg :: Nil,
      resolve = c ⇒ c.ctx.conferencesByEdition.get(c.arg(EditionArg)))
  ))

  val ConferenceSchema = Schema(QueryType)
}
