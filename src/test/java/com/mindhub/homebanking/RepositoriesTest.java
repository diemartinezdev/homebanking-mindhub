package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existAccounts(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }

    @Test
    public void existAccountByNumber(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number", is("VIN00000001"))));
    }

    @Test
    public void createAccount(){
        Client client = new Client("Alex","Filippo","alex@gmail.com","pupi123");
        clientRepository.save(client);
        Account account = new Account("VIN99999999",LocalDate.now(),1000.0,client);
        accountRepository.save(account);

        Account newAccount = accountRepository.findByNumber(account.getNumber());
        assertThat(newAccount, equalTo(account));
    }

    @Test
    public void existClients(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,is(not(empty())));
    }

    @Test
    public void existClientByEmail(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("email", is("melba@mindhub.com"))));
    }

    @Test
    public void createClient(){
        Client client = new Client("Cele","Martinez","celemartinez@gmail.com","Lalal123");
        clientRepository.save(client);
        Client newClient = clientRepository.findById(client.getId()).orElse(null);
        assertThat(newClient, equalTo(client));
    }

    @Test
    public void existCards(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,is(not(empty())));
    }

    @Test
    public void existCardByNumber(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("number", is("1234-1234-1234-1234"))));
    }

    @Test
    public void createCard(){
        Client client = new Client("Alex","Filippo","alex@gmail.com","pupi123");
        clientRepository.save(client);
        Card card = new Card(client.getFirstName()+" "+client.getLastName(),CardType.CREDIT,CardColor.TITANIUM,"4567-4567-4567-4567",987,LocalDate.now(),LocalDate.now().plusYears(4));
        cardRepository.save(card);

        Card newCard = cardRepository.findById(card.getId()).orElse(null);
        assertThat(newCard, equalTo(card));
    }

    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }

    @Test
    public void createTransaction(){
        Transaction transaction = new Transaction(TransactionType.CREDIT,500.5,"Testing", LocalDateTime.now());
        transactionRepository.save(transaction);

        Transaction newTransaction = transactionRepository.findById(transaction.getId()).orElse(null);

        assertThat(newTransaction, equalTo(transaction));
    }
}