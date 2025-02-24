package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@ComponentScan(basePackages = "com.example.service")
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public void setAccountService(AccountService accountService){
        this.accountService = accountService;
    }

    @Autowired
    public void setMessageService(MessageService messageService){
        this.messageService = messageService;
    }

    /**
     * ## 1: Our API should be able to process new User registrations.
     * 
     * As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. The body will 
     *      contain a representation of a JSON Account, but will not contain an accountId.
     * 
     * - The registration will be successful if and only if the username is not blank, the password is at least 4 
     *      characters long, and an Account with that username does not already exist. If all these conditions are met, 
     *      the response body should contain a JSON of the Account, including its accountId. The response status should 
     *      be 200 OK, which is the default. The new account should be persisted to the database.
     * - If the registration is not successful due to a duplicate username, the response status should be 409. (Conflict)
     * - If the registration is not successful for some other reason, the response status should be 400. (Client error)
     * 
     * @param account the Account object provided by the post request body
     * @return ResponseEntity with status code and body according to the above
     */
    @PostMapping("/register")
    public ResponseEntity<Account> userRegistration(@RequestBody Account account){
        //Attempt registration
        account = accountService.addNewAccount(account);
        if (account == null){
            //account creation failed due to duplicate username
            return ResponseEntity.status(409).body(null);
        }else if (account.equals(new Account())){
            //account creation failed for other reason
            return ResponseEntity.status(400).body(null);
        }else{
            //successful account creation
            return ResponseEntity.status(200).body(account);
        }
    }

    /**
     * ## 2: Our API should be able to process User logins.
     * 
     * As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. The request body will 
     *      contain a JSON representation of an Account.
     * 
     * - The login will be successful if and only if the username and password provided in the request body JSON match a 
     *      real account existing on the database. If successful, the response body should contain a JSON of the account 
     *      in the response body, including its accountId. The response status should be 200 OK, which is the default.
     * - If the login is not successful, the response status should be 401. (Unauthorized)
     * 
     * @param account the Account object provided by the post request body
     * @return ResponseEntity with status code and body according to the above
     */
    @PostMapping("/login")
    public ResponseEntity<Account> userLogin(@RequestBody Account account){
        //Attempt login
        account = accountService.loginWithAccount(account);
        if (account == null){
            //login attempt failed
            return ResponseEntity.status(401).body(null);
        }else{
            //successful login
            return ResponseEntity.status(200).body(account);
        }
    }

    /**
     * ## 3: Our API should be able to process the creation of new messages.
     * 
     * As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. The request body 
     *      will contain a JSON representation of a message, which should be persisted to the database, but will not 
     *      contain a messageId.
     * 
     * - The creation of the message will be successful if and only if the messageText is not blank, is not over 255 
     *      characters, and postedBy refers to a real, existing user. If successful, the response body should contain a 
     *      JSON of the message, including its messageId. The response status should be 200, which is the default. The 
     *      new message should be persisted to the database.
     * - If the creation of the message is not successful, the response status should be 400. (Client error)
     * 
     * @param message the Message object provided by the post request body
     * @return ResponseEntity with status code and body according to the above
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> postNewMessage(@RequestBody Message message){
        //Attempt to post the message
        message = messageService.addMessage(message);
        if (message == null){
            //message did not post to database
            return ResponseEntity.status(400).body(null);
        }else{
            //message was successfully posted
            return ResponseEntity.status(200).body(message);
        }
    }

    /** 
     * ## 4: Our API should be able to retrieve all messages.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
     * 
     * - The response body should contain a JSON representation of a list containing all messages retrieved from the 
     *      database. It is expected for the list to simply be empty if there are no messages. The response status 
     *      should always be 200, which is the default.
     * 
     * @return ResponseEntity with status code and body according to the above
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }// */

    /** 
     * ## 5: Our API should be able to retrieve a message by its ID.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{messageId}.
     * 
     * - The response body should contain a JSON representation of the message identified by the messageId. It is 
     *      expected for the response body to simply be empty if there is no such message. The response status should 
     *      always be 200, which is the default.
     * 
     * @param id the messageId provided by the endpoint GET path
     * @return ResponseEntity with status code and body according to the above
     */
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int id){
        Message message = messageService.getMessageById(id);
        if (message != null){
            return ResponseEntity.status(200).body(message);
        }else{
            return ResponseEntity.status(200).body(null);
        }
    }

    /**
     * ## 6: Our API should be able to delete a message identified by a message ID.
     * 
     * As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{messageId}.
     * 
     * - The deletion of an existing message should remove an existing message from the database. If the message existed, 
     *      the response body should contain the number of rows updated (1). The response status should be 200, which is 
     *      the default.
     * - If the message did not exist, the response status should be 200, but the response body should be empty. This is 
     *      because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should 
     *      respond with the same type of response.
     * 
     * @param id the messageId provided by the endpoint DELETE path
     * @return ResponseEntity with status code and body according to the above
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int id){
        int rows = messageService.deleteMessageById(id);
        if (rows == 0){
            //no message deleted
            return ResponseEntity.status(200).body(null);
        }else{
            //message deleted
            return ResponseEntity.status(200).body(rows);
        }
    }

    /**
     * ## 7: Our API should be able to update a message text identified by a message ID.
     * 
     * As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{messageId}. 
     *      The request body should contain a new messageText values to replace the message identified by messageId. The 
     *      request body can not be guaranteed to contain any other information.
     * 
     * - The update of a message should be successful if and only if the message id already exists and the new 
     *      messageText is not blank and is not over 255 characters. If the update is successful, the response body 
     *      should contain the number of rows updated (1), and the response status should be 200, which is the default. 
     *      The message existing on the database should have the updated messageText.
     * - If the update of the message is not successful for any reason, the response status should be 400. (Client error)
     * 
     * @param id the messageId provided by the endpoint PATCH path
     * @param message the Message object provided by the post request body
     * @return ResponseEntity with status code and body according to the above
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> patchMessageById(@PathVariable int id, @RequestBody Message message){
        //Attempt to update message
        int rows = messageService.updateMessage(id, message);
        if (rows == 0){
            //message failed to update
            return ResponseEntity.status(400).body(null);
        }else{
            //message was successfully updated
            return ResponseEntity.status(200).body(rows);
        }
    }

    /**
     * ## 8: Our API should be able to retrieve all messages written by a particular user.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{accountId}/messages.
     * 
     * - The response body should contain a JSON representation of a list containing all messages posted by a particular 
     *      user, which is retrieved from the database. It is expected for the list to simply be empty if there are no 
     *      messages. The response status should always be 200, which is the default.
     * 
     * @param id the accountId provided by the endpoint GET path
     * @return ResponseEntity with status code and body according to the above
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable int id){
        return ResponseEntity.status(200).body(messageService.getAllMessagesByAccountId(id));
    }
}
