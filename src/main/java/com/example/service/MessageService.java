package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.ClientErrorException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
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
     * @return message object with messageId if successful
     * @throws ClientErrorException if criteria not met
     */
    public Message addMessage(Message message) throws ClientErrorException {
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255 
                || !(accountRepository.existsById(message.getPostedBy()))){
            throw new ClientErrorException("Message not created");
        }
        //add message into DB and return message with messageId
        message = messageRepository.save(message);
        return message;
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
     * @return list of all messages in database
     */
    public List<Message> getAllMessages() {
        //get list of messages from DB
        return messageRepository.findAll();
    }

    /** 
     * ## 5: Our API should be able to retrieve a message by its ID.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{messageId}.
     * 
     * - The response body should contain a JSON representation of the message identified by the messageId. It is 
     *      expected for the response body to simply be empty if there is no such message. The response status should 
     *      always be 200, which is the default.
     * 
     * @param messageId the messageId provided by the endpoint GET path
     * @return message object identified by messageId, if it exssts, {@code null} otherwise
     */
    public Message getMessageById(int messageId) {
        //get message from DB
        if(messageRepository.existsById(messageId)){
            return messageRepository.findById(messageId).get();
        }
        return null;
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
     * @param messageId the messageId provided by the endpoint DELETE path
     * @return number of rows updated
     */
    public int deleteMessageById(int messageId) {
        //delete message from DB if it exists
        int rows = 0;
        if(messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            rows = 1;
        }
        return rows;
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
     * @param messageId the messageId provided by the endpoint PATCH path
     * @param newMessage the Message object provided by the post request body
     * @return number of rows updated (1)
     * @throws ClientErrorException if criteria not met or messageId does not exist
     */
    public int updateMessage(int messageId, Message newMessage) throws ClientErrorException {
        if (newMessage.getMessageText().isBlank() || newMessage.getMessageText().length() > 255 || !(messageRepository.existsById(messageId))){
            throw new ClientErrorException("Message not updated");
        }
        //update message in DB
        Message oldMessage = messageRepository.findById(messageId).get();
        oldMessage.setMessageText(newMessage.getMessageText());
        messageRepository.save(oldMessage);
        return 1;
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
     * @param accountId the accountId provided by the endpoint GET path
     * @return list of all messages written by account identified by accountId
     */
    public List<Message> getAllMessagesByAccountId(int accountId) {
        //get list of messages postedBy id
        return messageRepository.findByPostedBy(accountId);
    }
}
