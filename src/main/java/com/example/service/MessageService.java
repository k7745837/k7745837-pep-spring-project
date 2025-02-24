package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Message;

@Service
public class MessageService {

    /**
     * TODO: ## 3: Our API should be able to process the creation of new messages.
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
     * @return message object with messageId if successful, {@code null} otherwise
     */
    public Message addMessage(Message message) {
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255 || message.getPostedBy().equals(message/*check if postedBy refers to a real, existing user*/)){
            //return null if message fails basic criteria
            return null;
        }
        //add message into DB and return message with messageId
        message = message;
        return message;
    }

    /** 
     * TODO: ## 4: Our API should be able to retrieve all messages.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
     * 
     * - The response body should contain a JSON representation of a list containing all messages retrieved from the 
     *      database. It is expected for the list to simply be empty if there are no messages. The response status 
     *      should always be 200, which is the default.
     * 
     * @return list of all messages in database
     */
    public List<Message> getAllMessages() {
        //get list of messages from DB
        return null;
    }

    /** 
     * TODO: ## 5: Our API should be able to retrieve a message by its ID.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{messageId}.
     * 
     * - The response body should contain a JSON representation of the message identified by the messageId. It is 
     *      expected for the response body to simply be empty if there is no such message. The response status should 
     *      always be 200, which is the default.
     * 
     * @param id the messageId provided by the endpoint GET path
     * @return message object identified by messageId, if it exssts, {@code null} otherwise
     */
    public Message getMessageById(int id) {
        //get message from DB
        Message message = new Message();
        return message;
    }

    /**
     * TODO:## 6: Our API should be able to delete a message identified by a message ID.
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
     * @return number of rows updated
     */
    public int deleteMessageById(int id) {
        //delete message from DB if it exists
        int rows = 0;
        return rows;
    }

    /**
     * TODO: ## 7: Our API should be able to update a message text identified by a message ID.
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
     * @return number of rows updated
     */
    public int updateMessage(int id, Message message) {
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255 || id!=message.getMessageId()/*check if messageId doesn't exist in DB*/){
            return 0;
        }
        //update message in DB
        int rows = 0;
        return rows;
    }

    /**
     * TODO: ## 8: Our API should be able to retrieve all messages written by a particular user.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{accountId}/messages.
     * 
     * - The response body should contain a JSON representation of a list containing all messages posted by a particular 
     *      user, which is retrieved from the database. It is expected for the list to simply be empty if there are no 
     *      messages. The response status should always be 200, which is the default.
     * 
     * @param id the accountId provided by the endpoint GET path
     * @return list of all messages written by account identified by accountId
     */
    public List<Message> getAllMessagesByAccountId(int id) {
        //get list of messages postedBy id
        return null;
    }
}
