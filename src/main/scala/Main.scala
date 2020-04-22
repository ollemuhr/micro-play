import io.helidon.config.Config
import io.helidon.grpc.server.{ GrpcRouting, GrpcServer, GrpcServerConfiguration }

object Main {
  def main(args: Array[String]) {
    start
  }

  def start: GrpcServer = {
    val config = Config.create
    val serverConfig =
      GrpcServerConfiguration.builder(config.get("grpc")).build();
    val grpcServer = GrpcServer.create(serverConfig, createRouting());

    grpcServer
      .start()
      .thenAccept { s =>
        println(s"gRPC server is UP! http://localhost: ${s.port()}");
        s.whenShutdown().thenRun(() => println("gRPC server is DOWN. Good bye!"));
      }
      .exceptionally { t =>
        println(s"Startup failed: ${t.getMessage}");
        t.printStackTrace(System.err);
        return null;
      };
    grpcServer
  }

  def createRouting(): GrpcRouting =
    GrpcRouting.builder().register(new EmployeeService).build()
}
