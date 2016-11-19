package github.dboroujerdi.sched.app.parse

import cats.data.Xor
import github.dboroujerdi.sched.api.model.Types.Schedule
import github.dboroujerdi.sched.app.FutureMaybe
import github.dboroujerdi.sched.app.parse.html.ExceptionalScrapeError
import github.dboroujerdi.sched.app.parse.html.Types.ErrorOrEvent
import net.ruippeixotog.scalascraper.model.Document

trait ParserComponent {

  trait Parser {

    protected def logAndFilterFailures(list: Seq[ErrorOrEvent]): Option[Schedule] = {
      list.filter(_.isLeft).foreach {
        case Xor.Left(error@ExceptionalScrapeError(_, _)) => println("Unable to parse: ", error)
        case _ =>
      }

      Option {
        list.collect {
          case Xor.Right(event) => event
        }
      }
    }

    def parse(doc: Document): FutureMaybe[Schedule]
  }

  val parser: Parser
}