package com.icarus.grpc.blog.client;

import com.proto.blog.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {

    public static void main(String[] args) {
        System.out.println("Hello I'm a gRPC client for blog");

        BlogClient main = new BlogClient();
        main.run();
    }

    private void run() {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        BlogServiceGrpc.BlogServiceBlockingStub blogClient = BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = Blog.newBuilder()
                .setAuthorId("Stephane")
                .setTitle("New blog!")
                .setContent("Hello world this is my first blog!")
                .build();

        CreateBlogResponse createResponse = blogClient.createBlog(
                CreateBlogRequest.newBuilder().setBlog(blog).build()
        );
        System.out.println("Received create blog response");
        System.out.println(createResponse.toString());

        String blogId = createResponse.getBlog().getId();
        System.out.println("Reading blog...");
        ReadBlogResponse readBlogResponse = blogClient.readBlog(ReadBlogRequest.newBuilder()
                .setBlogId(blogId)
                .build());
        System.out.println(readBlogResponse);

        // trigger a not found error
//        System.out.println("Reading blog with non existing id...");
//        ReadBlogResponse readBlogResponseNotFound = blogClient.readBlog(ReadBlogRequest.newBuilder()
//                .setBlogId("fake-id")
//                .build());
//        System.out.println(readBlogResponseNotFound);

        Blog newBlog = Blog.newBuilder()
                .setId(blogId)
                .setAuthorId("Changed author")
                .setTitle("New blog (updated)!")
                .setContent("Hello world this is my first blog! I've added some more content")
                .build();

        System.out.println("Updating blog...");
        UpdateBlogResponse updateBlogResponse = blogClient.updateBlog(
                UpdateBlogRequest.newBuilder().setBlog(newBlog).build());
        System.out.println("Updated blog");
        System.out.println(updateBlogResponse.toString());

        System.out.println("Deleting blog...");
        DeleteBlogResponse deleteBlogResponse = blogClient.deleteBlog(
                DeleteBlogRequest.newBuilder().setBlogId(blogId).build());
        System.out.println("Deleted blog");

//        System.out.println("Reading blog...");
//        ReadBlogResponse readBlogResponseAfterDeletion = blogClient.readBlog(ReadBlogRequest.newBuilder()
//                .setBlogId(blogId)
//                .build());

        // we list the blog in our database
        blogClient.listBlog(ListBlogRequest.newBuilder().build()).forEachRemaining(
                listBlogResponse -> System.out.println(listBlogResponse.getBlog().toString())
        );
    }
}
