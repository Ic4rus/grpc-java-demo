package com.icarus.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        System.out.println("Creating stub");
        // old dummy
//        DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);
//        DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);

        // create a greet service client (blocking - synchronous)
        GreetingServiceGrpc.GreetingServiceBlockingStub greetClient = GreetingServiceGrpc.newBlockingStub(channel);

        // Unary
//        // create a protocol buffer greeting message
//        Greeting greeting = Greeting.newBuilder()
//                .setFirstName("Stephane")
//                .setLastName("Maarek")
//                .build();
//
//        // do same for a GreetRequest
//        GreetRequest greetRequest = GreetRequest.newBuilder()
//                .setGreeting(greeting)
//                .build();
//
//        // call the RPC and get back a GreetResponse (protocol buffers)
//        GreetResponse greetResponse = greetClient.greet(greetRequest);
//
//        System.out.println(greetResponse.getResult());

        // Server streaming
        // we prepare the request
        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Stephane").build())
                .build();

        // we stream the response (in a blocking manner)
        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });

        // do something
        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}
