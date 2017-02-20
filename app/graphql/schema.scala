package graphql

import repo.ContentRepo
import sangria.schema._
import customScalars._
import sangria.marshalling.ScalaInput
import views.{Conference, Edition}

object schema {
  val EditionArg = Argument("edition", OptionInputType(Edition.graphqlType), ScalaInput.scalaInput(Edition.Berlin2017))

  val QueryType = ObjectType("Query", fields[ContentRepo, Unit](
    Field("conference", Conference.graphqlType,
      arguments = EditionArg :: Nil,
      resolve = c ⇒ c.ctx.conference)
  ))

  val ConferenceSchema = Schema(QueryType)
}
