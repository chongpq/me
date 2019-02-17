## Financial Transactions Analyser

### Run
Assuming you are executing on a POSIX box, Windows machines are very similar. Install both [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [apache-maven-3.5.0](https://maven.apache.org/download.cgi). Go to the me project and run `apache-maven-3.5.0/bin/mvn clean install` to build this project. This will download all dependencies and create the jar in me/target folder. To run this project type `java -jar ./target/financial-transaction-analyser-1.0-SNAPSHOT.jar <file-location>`. This will execute the program. There is an input file in the test resources folder so you could use the following command
```
java -jar ./target/financial-transaction-analyser-1.0-SNAPSHOT.jar ./src/test/resources/input.csv
```

Follow the command prompts to enter in account id , from date, to date and you will get the expected results. Press `ctrl-c` at anytime to exit.

### Testing
The results of testing can be viewed after the running `mvn clean install` by viewing ./target/site/jacoco/index.html

### Assumptions
* Reversals reverse the whole transaction by TransactionId even if the transaction details don't match up. This means that if there are two rows with the same TransactionId both rows will be removed by the one reversal.
* We are using the same TimeZone/Locale as csv writer.
* That the time boundaries are excluded from the RelativeAccount calculation. So it means that the time is between the from and to dates and not including them.
* Input file and records are all in a valid format - so I depend on this in the Transaction constructor. Runtime exception are being thrown instead of being properly named and handled.
* Transaction are recorded in order - I don't depend on this one hence the use of foreach in the ReversalScrubber and the fact that I'm cycling through all transactions and checking if they're needed for each query. I've explored coding an improvement in [https://github.com/chongpq/me/tree/createAt](https://github.com/chongpq/me/tree/createAt) however this code is incomplete at the moment.

### Design
Given that, `if a transaction has a reversing transaction, this transaction should be omitted from the calculation, even if the reversing transaction is outside the given time frame`, means that we can preprocess the transactions beforehand. So we scrub the records of all reversals. The scrubbing is handled by the ReversalScrubber class. Then we can process any queries.

The query analysing is done by the QueryAnalyser class. It uses filter functions to get the right rows to process by filtering on the createAt, fromAccount and toAccount. I've kept them in 2 filter functions.
1. createAt - as this may be replaced with a better way - see [https://github.com/chongpq/me/tree/createAt](https://github.com/chongpq/me/tree/createAt)
2. fromAccount and toAccount

We need to calculate count and the relative account balance - this is handled in the RelativeAccount class, with the method of note being the RelativeAccount::addTransaction.

All these classes are coordinated by the main class FinancialTransactionAnalyser. It separates all the System.out, System.in and System.err handling. All these classes communicate via the Transaction class.

I've kept the RelativeAccount and the Transaction class immutable by design.
