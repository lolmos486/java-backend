package com.revature.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exception.InvalidParameterException;
import com.revature.model.Account;
import com.revature.model.Transaction;
import com.revature.model.User;
import com.revature.service.AccountService;
import com.revature.service.TransactionService;
import com.revature.service.UserService;
import io.javalin.Javalin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TransactionController implements Controller {
    private final TransactionService transactionService;
    private UserService userService;
    private AccountService accountService;

    public TransactionController() {
        userService = new UserService();
        accountService = new AccountService();
        transactionService = new TransactionService();
    }


    @Override
    @SuppressWarnings("unchecked")
    public void mapEndpoints(Javalin app) {
        app.post("trx/accounts", ctx -> {
            try {
                HttpServletRequest req = ctx.req;
                HttpSession session = req.getSession();
                String role = (String) session.getAttribute("userRole");
                int uId = (int) session.getAttribute("userId");
                ObjectMapper om = new ObjectMapper();
                Map<String, String> newTransaction = om.readValue(ctx.body(), Map.class);
                boolean owner = accountService.isOwnerOfAccount(uId, Integer.parseInt(newTransaction.get("sendingId")));
                if (Objects.equals(role, "2") || owner) {
                    ctx.json(transactionService.transferBetweenAccounts(newTransaction, uId));
                    ctx.status(201);
                }
            } catch (InvalidParameterException e) {
                ctx.json(e.getMessages());
                System.out.println(e.getMessages());
                ctx.status(400);
            } catch (Exception e) {
                ctx.json(e.getMessage());
                System.out.println(e.getMessage());
                ctx.status(400);
            }

        });

        app.post("/trx", ctx -> {
            try {
                HttpServletRequest req = ctx.req;
                HttpSession session = req.getSession();
                Integer uId = (Integer) session.getAttribute("userId");
                String email = (String) session.getAttribute("email");

                ObjectMapper om = new ObjectMapper();
                Map<String, String> newTransaction = om.readValue(ctx.body(), Map.class);
                boolean owner = accountService.isOwnerOfAccount(uId, Integer.parseInt(newTransaction.get("sendingId")));
                if (owner) {
                    ctx.json(transactionService.sendMoney(newTransaction, uId, email));
                    ctx.status(200);
                }
            } catch (InvalidParameterException e) {
                ctx.json(e.getMessages());
                System.out.println(e.getMessages());
                ctx.status(400);
            } catch (Exception e) {
                ctx.json(e.getMessage());
                System.out.println(e.getMessage());
                ctx.status(400);
            }
        });


        app.post("/trx/req", ctx -> {

            try {
                HttpServletRequest req = ctx.req;
                HttpSession session = req.getSession();
                Integer uId = (Integer) session.getAttribute("userId");
                String email = (String) session.getAttribute("email");

                ObjectMapper om = new ObjectMapper();
                Map<String, String> newTransaction = om.readValue(ctx.body(), Map.class);
                boolean owner = accountService.isOwnerOfAccount(uId, Integer.parseInt(newTransaction.get("receivingId")));
                if (owner) {
                    ctx.json(ctx.json(transactionService.requestAmount(newTransaction, uId, email)));
                    ctx.status(200);
                }
            } catch (Exception e) {
                ctx.json(e.getMessage());
                System.out.println(e.getMessage());
                ctx.status(400);
            }

        });


        app.put("/trx/req", ctx -> {
//            endpoint expects JSON { "statusId": "{2 for approved, 3 for denied}", "transactionId": "{trxId}"}
            try {
                Transaction tr = ctx.bodyAsClass(Transaction.class);
                System.out.println(tr);
                int transactionId = tr.getTransactionId();
                String email = userService.getRequesteeByTransactionId(transactionId);
                HttpServletRequest req = ctx.req;
                HttpSession session = req.getSession();
                String emailSignedInUser = (String) session.getAttribute("email");
                String role = (String) session.getAttribute("userRole");

                if (Objects.equals(email, emailSignedInUser) || Objects.equals(role, "2")) {
                    ObjectMapper om = new ObjectMapper();
                    Map<String, String> newTransaction = om.readValue(ctx.body(), Map.class);
                    try {
                        ctx.json(transactionService.handleRequestAmount(newTransaction));
                        ctx.status(201);
                    } catch (InvalidParameterException e) {
                        ctx.json(e.getMessages());
                        ctx.status(400);
                    }
                } else {
                    ctx.json("User Not Found");
                    ctx.status(404);
                }
            } catch (Exception e) {
                ctx.json(e.getMessage());
                ctx.status(400);
            }

        });

        app.get("/trx", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String role = (String) session.getAttribute("userRole");
            if (role.equals("2")) {
                ctx.json(transactionService.getAllTransactions());
                ctx.status(200);
            }
        });
        app.get("/trx/account/{aid}", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            int aid = Integer.parseInt(ctx.pathParam("aid"));
            ctx.json(transactionService.getAllTransactions(aid));
            ctx.status(200);

        });

        app.get("/trx/{requesterId}/requester", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String role = (String) session.getAttribute("userRole");
            String requesterId = ctx.pathParam("requesterId");
            if (role.equals("2")) {
                ctx.json(transactionService.getAllTransactionsByRequesterId(requesterId));
                ctx.status(200);
            }
        });

        app.get("/trx/{senderId}/sender", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String role = (String) session.getAttribute("userRole");
            String senderId = ctx.pathParam("senderId");
            if (role.equals("2")) {
                ctx.json(transactionService.getAllTransactionsBySenderId(senderId));
                ctx.status(200);
            }
        });


        app.get("/trx/{receivingId}/receiver", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String emailSignedInUser = (String) session.getAttribute("email");
            String role = (String) session.getAttribute("userRole");
            String receivingId = ctx.pathParam("receivingId");
            List<String> emails = userService.getReceiverByTransactionId(Integer.parseInt(receivingId));
            if (emails.contains(emailSignedInUser) || role.equals("2")) {
                ctx.json(transactionService.getAllTransactionsByReceivingId(receivingId));
                ctx.status(200);
            }
        });

        app.get("/trx/status/outgoing/{status-name}/{aId}", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String role = (String) session.getAttribute("userRole");
            String emailSignedInUser = (String) session.getAttribute("email");
            String statusName = ctx.pathParam("status-name").toUpperCase();
            int aId = Integer.parseInt(ctx.pathParam("aId"));
            if (role.equals("2") || accountService.isOwnerOfAccount(userService.getUserByEmail(emailSignedInUser).getUserId(), aId)) {
                ctx.json(transactionService.getAllOutgoingTransactionsByStatusName(statusName, aId));
                ctx.status(200);
            }
        });

        app.get("/trx/status/incoming/{status-name}/{aId}", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String role = (String) session.getAttribute("userRole");
            String emailSignedInUser = (String) session.getAttribute("email");
            String statusName = ctx.pathParam("status-name").toUpperCase();
            int aId = Integer.parseInt(ctx.pathParam("aId"));
            if (role.equals("2") || accountService.isOwnerOfAccount(userService.getUserByEmail(emailSignedInUser).getUserId(), aId)) {
                ctx.json(transactionService.getAllIncomingTransactionsByStatusName(statusName, aId));
                ctx.status(200);
            }
        });

        app.get("/trx/{descriptionId}/description", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String role = (String) session.getAttribute("userRole");
            String description = ctx.pathParam("descriptionId");
            if (role.equals("2")) {
                ctx.json(transactionService.getAllTransactionsByDescription(description));
                ctx.status(200);
            }
        });

        app.get("/trx/income-by-account/{aId}/{month}/{year}", ctx -> {
            try {
                HttpServletRequest req = ctx.req;
                HttpSession session = req.getSession();
                String emailSignedInUser = (String) session.getAttribute("email");
                int uId = (int) session.getAttribute("userId");
                String role = (String) session.getAttribute("userRole");
                int aId = Integer.parseInt(ctx.pathParam("aId"));
                int month = Integer.parseInt(ctx.pathParam("month"));
                int year = Integer.parseInt(ctx.pathParam("year"));
                if (month == 0 && year == 0) {
                    month = transactionService.getCurrentMonth();
                    year = transactionService.getCurrentYear();
                }
                List<Account> accounts = accountService.getAccountsByEmail(emailSignedInUser);
                List<Integer> accountIds = new ArrayList<Integer>();
                for (Account a : accounts)
                    accountIds.add(a.getAccountId());
                if (accountIds.contains(aId) || Objects.equals(role, "2")) {
                    ctx.json(transactionService.trackAccountIncome(uId, aId, month, year));
                    ctx.status(200);
                } else {
                    ctx.json("Access Denied");
                    ctx.status(401);
                }
            } catch (Exception e) {
                ctx.json(e.getMessage());
                ctx.status(400);
            }
        });


        app.get("/trx/income-by-account/{aId}/", ctx -> {
            try {
                HttpServletRequest req = ctx.req;
                HttpSession session = req.getSession();
                String emailSignedInUser = (String) session.getAttribute("email");
                String role = (String) session.getAttribute("userRole");
                int aId = Integer.parseInt(ctx.pathParam("aId"));
                int uId = 0;
                boolean owns = false;
                if (!Objects.equals(role, "2")) {
                    uId = userService.getUserByEmail(emailSignedInUser).getUserId();
                    owns = accountService.isOwnerOfAccount(uId, aId);
                }
                if (Objects.equals(role, "2") || owns) {
                    ctx.json(transactionService.trackAllTimeAccountIncome(uId, aId));
                    ctx.status(200);
                } else {
                    ctx.json("Access Denied");
                    ctx.status(401);
                }
            } catch (Exception e) {
                ctx.json(e.getMessage());
                ctx.status(400);
            }
        });

        app.get("/trx/income-by-user/{uId}/{month}/{year}", ctx -> {
            try {
                HttpServletRequest req = ctx.req;
                HttpSession session = req.getSession();
                String emailSignedInUser = (String) session.getAttribute("email");
                String role = (String) session.getAttribute("userRole");
                int uId = Integer.parseInt(ctx.pathParam("uId"));
                int month = Integer.parseInt(ctx.pathParam("month"));
                int year = Integer.parseInt(ctx.pathParam("year"));
                if (month == 0 && year == 0) {
                    month = transactionService.getCurrentMonth();
                    year = transactionService.getCurrentYear();
                }
                if (uId == userService.getUserByEmail(emailSignedInUser).getUserId() || Objects.equals(role, "2")) {
                    ctx.json(transactionService.trackUserIncome(uId, month, year));
                    ctx.status(200);
                } else {
                    ctx.json("Access Denied");
                    ctx.status(401);
                }
            } catch (Exception e) {
                ctx.json(e.getMessage());
                ctx.status(400);
            }
        });

        app.get("/trx/income-by-user/{uId}", ctx -> {
            try {
                HttpServletRequest req = ctx.req;
                HttpSession session = req.getSession();
                String emailSignedInUser = (String) session.getAttribute("email");
                String role = (String) session.getAttribute("userRole");
                int uId = Integer.parseInt(ctx.pathParam("uId"));
                if (uId == userService.getUserByEmail(emailSignedInUser).getUserId() || Objects.equals(role, "2")) {
                    ctx.json(transactionService.trackAllTimeUserIncome(uId));
                    ctx.status(200);
                } else {
                    ctx.json("Access Denied");
                    ctx.status(401);
                }
            } catch (Exception e) {
                ctx.json(e.getMessage());
                ctx.status(400);
            }
        });

    }

}


