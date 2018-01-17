package com.vmware.connectors.slack;

import com.vmware.connectors.common.json.JsonDocument;
import com.vmware.connectors.common.utils.Async;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestOperations;
import rx.Observable;
import rx.Single;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@RestController
public class SlackController {

    private String baseUrl = "https://vmware.slack.com";
    private String slackOAuth = "";

    private final AsyncRestOperations rest;

    @Autowired
    public SlackController(AsyncRestOperations rest) {
        this.rest = rest;
    }

    @PostMapping("/channels/create")
    public Single<ResponseEntity<HttpStatus>> newSlackChannel(
            @RequestParam("name") String channelName,
            @RequestParam("message") String message,
            @RequestParam("emails") List<String> userEmails
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + slackOAuth);
        headers.set(HttpHeaders.CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);

        Single<SlackChannel> slackChannel = createChannel(channelName, headers);

        return slackChannel
                .flatMap(channelObj -> inviteUsers(channelObj, userEmails, headers))
                .flatMap(channelObj -> postComment(channelObj, message, headers))
                .map(ResponseEntity::new);
    }

    private Single<HttpStatus> postComment(SlackChannel channel, String message, HttpHeaders headers) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("channel", channel.getId());
        map.add("text", message);
        ListenableFuture<ResponseEntity<JsonDocument>> future = rest.exchange(
                "{baseUrl}/api/chat.postMessage", POST,
                new HttpEntity<>(map, headers), JsonDocument.class, baseUrl);
        return Async.toSingle(future)
                .map(entity -> {
                    System.out.println("Message posted");
                    return entity.getStatusCode();
                });
    }

    /*
     * Invite users by email to a slack channel.
     */
    private Single<SlackChannel> inviteUsers(SlackChannel channel,
                                             List<String> userEmails, HttpHeaders headers) {

        Single<List<SlackUser>> slackUsers = Observable.from(userEmails)
                .flatMap(email -> findSlackUser(email, headers))
                .toList()
                .toSingle();

        return slackUsers
                .flatMap(sUsers -> inviteSlackUsers(channel, sUsers, headers));
    }

    /*
     * Invite users by slack Id to a channel.
     */
    private Single<SlackChannel> inviteSlackUsers(SlackChannel channel,
                                                  List<SlackUser> slackUsers, HttpHeaders headers) {
        String usersFormValue = slackUsers.stream()
                                    .map(SlackUser::getId)
                                    .collect(Collectors.joining(","));
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("channel", channel.getId());
        map.add("users", usersFormValue);

        ListenableFuture<ResponseEntity<JsonDocument>> future = rest.exchange(
                "{baseUrl}/api/channels.invite", POST,
                new HttpEntity<>(map, headers), JsonDocument.class, baseUrl);

        return Async.toSingle(future)
                    .map(entity -> {
                        System.out.println(">> Users are invited");
                        return channel;
                    });
    }

    private Single<SlackChannel> createChannel(String name, HttpHeaders headers) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", name);
        ListenableFuture<ResponseEntity<JsonDocument>> future = rest.exchange(
                "{baseUrl}/api/channels.create", POST,
                new HttpEntity<>(map, headers), JsonDocument.class, baseUrl);
        return Async.toSingle(future)
                .map(entity -> {
                    String id = entity.getBody().read("$.channel.id");
                    return new SlackChannel(id);
                });
    }

    private Observable<SlackUser> findSlackUser(
            String emailId, HttpHeaders headers) {
        /*
         * Need to make network call for every email id, to find the slack user.
         */
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", emailId);
        ListenableFuture<ResponseEntity<JsonDocument>> future = rest.exchange(
                "{baseUrl}/api/users.lookupByEmail", POST,
                new HttpEntity<>(map, headers), JsonDocument.class, baseUrl);
        return Async.toSingle(future)
                .map(entity -> {
                    String id = entity.getBody().read("$.user.id");
                    System.out.println(">> Slack user id : "+id);
                    return new SlackUser(id);
                }).toObservable();
    }

    private class SlackUser {
        private String id;

        private SlackUser(String id) {
            this.id = id;
        }

        private String getId() {
            return id;
        }

    }

    private class SlackChannel {
        private String id;

        private SlackChannel(String id) {
            this.id = id;
        }

        private String getId() {
            return id;
        }
    }
}
