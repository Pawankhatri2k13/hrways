package com.smallworld;

import com.smallworld.data.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class TransactionDataFetcher {
    private List<Transaction> transactions;

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        double totalAmount = 0.0;

        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmount();
        }

        return totalAmount;
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        double totalAmount = 0.0;

        for (Transaction transaction : transactions) {
            if (transaction.getSenderFullName().equals(senderFullName)) {
                totalAmount += transaction.getAmount();
            }
        }

        return totalAmount;
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        if (transactions.isEmpty()) {
            throw new IllegalStateException("Transaction list is empty");
        }
        double maxAmount = 0.0;

        for (Transaction transaction : transactions) {
            double currentAmount = transaction.getAmount();
            if (currentAmount > maxAmount) {
                maxAmount = currentAmount;
            }
        }
        return maxAmount;
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        Set<String> uniqueClients = new HashSet<>();

        for (Transaction transaction : transactions) {
            uniqueClients.add(transaction.getSenderFullName());
            uniqueClients.add(transaction.getBeneficiaryFullName());
        }

        return uniqueClients.size();
    }
    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        for (Transaction transaction : transactions) {
            if (transaction.getSenderFullName().equalsIgnoreCase(clientFullName) || transaction.getBeneficiaryFullName().equalsIgnoreCase(clientFullName)) {
                if (transaction.isIssueSolved() == false) {
                    return true; // There is at least one open compliance issue
                }
            }
        }
        return false; // No open compliance issues for the specified client
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        Map<String, Transaction> transactionsByBeneficiary = new HashMap<>();

        for (Transaction transaction : transactions) {
            String beneficiaryName = transaction.getBeneficiaryFullName();
            transactionsByBeneficiary.put(beneficiaryName, transaction);
        }

        return transactionsByBeneficiary;
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        Set<Integer> unsolvedIssueIds = new HashSet<>();
        for (Transaction transaction : transactions) {
            if (!transaction.isIssueSolved()) {
                unsolvedIssueIds.add(transaction.getIssueId());
            }
        }
        return unsolvedIssueIds;
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        List<String> solvedIssueMessages = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.isIssueSolved()) {
                solvedIssueMessages.add(transaction.getIssueMessage());
            }
        }
        return solvedIssueMessages;
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        // Sort transactions by amount in descending order
        List<Transaction> sortedTransactions = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .collect(Collectors.toList());

        // Now We need to get the top 3 transactions
        return sortedTransactions.stream().limit(3).collect(Collectors.toList());
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        Map<String, Double> totalSentAmountBySender = new HashMap<>();

        for (Transaction transaction : transactions) {
            String senderFullName = transaction.getSenderFullName();
            double transactionAmount = transaction.getAmount();

            totalSentAmountBySender.put(senderFullName, totalSentAmountBySender.getOrDefault(senderFullName, 0.0) + transactionAmount);
        }

        // Find the sender with the most total sent amount
        String topSender = null;
        double maxTotalSentAmount = 0.0;

        for (Map.Entry<String, Double> entry : totalSentAmountBySender.entrySet()) {
            if (entry.getValue() > maxTotalSentAmount) {
                topSender = entry.getKey();
                maxTotalSentAmount = entry.getValue();
            }
        }

        return Optional.ofNullable(topSender);
    }

}
