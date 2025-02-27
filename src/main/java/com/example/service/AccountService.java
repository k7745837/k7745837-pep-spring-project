package com.example.service;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.ClientErrorException;
import com.example.exception.ConflictException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    AccountRepository accountRepository;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
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
     * @return account object with accountId if successful
     * @throws ClientErrorException if basic criteria are not met
     * @throws ConflictException if username is already in use
     */
    public Account addNewAccount(Account account) throws ClientErrorException, ConflictException {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4){
            //if provided account fails basic criteria
            throw new ClientErrorException("Registration criteria not met");
        }else if (accountRepository.existsByUsername(account.getUsername())){
            //if account with username already exists
            throw new ConflictException("Duplicate username.");
        }else{
            //add account to DB and return account with accountId
            account = accountRepository.save(account);
            return account;
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
     * @return account object with accountId if successful, otherwise throws javax.security.sasl.AuthenticationException
     * @throws AuthenticationException 
     */
    public Account loginWithAccount(Account account) throws AuthenticationException {
        //check DB for matching username and password and return result of query
        account = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())
                .orElseThrow(() -> new AuthenticationException("Login unsuccessful."));
        return account;
    }
}
