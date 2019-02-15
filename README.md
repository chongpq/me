##Financial Transactions Analyser

###Assumptions
Reversals reverse the whole transaction even if the details don't match up  - by TransactionId 

We are on the same TimeZone/Locale as csv writer.

That the time boundaries are excluded from the RelativeAccount calculation.

Input file and records are all in a valid format - so I depend on this in the Transaction constructor. Runtime exception are being thrown instead of being handled.

Transaction are recorded in order - I don't depend on this one hence the use of foreach in the ReversalScrubber. I'm cycling through all transactions and checking if they're needed for each query.

###Design
scrub the records of all reversals.

Then we are ready for the queries.

use stream filters to remove unneeded createdAt to find the range in question.
stream filters to remove unnessecary accounts (fromAccount, toAccount). We must get 2 things count and the relative account balance.

process the debit and credit for the relative account balance.
