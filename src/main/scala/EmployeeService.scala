import java.time.LocalDate

import com.github.ollemuhr.proto.Employees
import com.github.ollemuhr.proto.Employees.{ ByIdRequest, Employee }
import io.grpc.stub.StreamObserver
import io.helidon.grpc.core.ResponseHelper
import io.helidon.grpc.server.{ GrpcService, ServiceDescriptor }

class EmployeeService extends GrpcService {
  override def update(rules: ServiceDescriptor.Rules): Unit =
    rules
      .proto(Employees.getDescriptor)
      .unary("ById", (req, ob) => byId(req, ob))

  private def byId(req: ByIdRequest, observer: StreamObserver[Employee]): Unit = {
    val employee = Employees.Employee
      .newBuilder()
      .setId(req.getId)
      .setBirthDate(LocalDate.now().minusYears(20).toString)
      .setDepartment("a-department")
      .setEmail("a@b.com")
      .setFirstName("Olle")
      .setLastName("Muhr")
      .setPhone("1234567")
      .setTitle("developer")
      .build()
    ResponseHelper.complete(observer, employee)
  }
}
