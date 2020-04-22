import java.util.concurrent.CompletableFuture

import com.github.ollemuhr.proto.EmployeeServiceGrpc
import com.github.ollemuhr.proto.Employees.{ ByIdRequest, Employee }
import io.grpc.{ Channel, ManagedChannelBuilder }
import io.helidon.grpc.client.{ ClientServiceDescriptor, GrpcServiceClient }
import io.helidon.grpc.server.GrpcServer
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class ATest extends AnyWordSpecLike with Matchers with BeforeAndAfterAll {
  var server: GrpcServer        = _
  var client: GrpcServiceClient = _
  override def beforeAll(): Unit = {
    server = Main.start
    val descriptor =
      ClientServiceDescriptor.builder(EmployeeServiceGrpc.getServiceDescriptor).build()

    val channel: Channel =
      ManagedChannelBuilder
        .forAddress("localhost", 1408)
        .usePlaintext
        .asInstanceOf[ManagedChannelBuilder[_]]
        .build()

    client = GrpcServiceClient.create(channel, descriptor)
  }

  override protected def afterAll(): Unit = {
    super.afterAll()
    server.shutdown()
  }
  "a grpc client" should {
    "get employee by id" in {
      val f: CompletableFuture[Employee] =
        client.unary("ById", ByIdRequest.newBuilder.setId("1").build()).toCompletableFuture
      val employee = f.get()
      println(employee)
      employee.getId should be("1")
      employee.getFirstName should be("Olle")
    }
  }
}
