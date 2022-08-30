package com.revature.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exception.InvalidParameterException;
import com.revature.model.User;
import com.revature.service.AccountService;
import com.revature.service.UserService;
import io.javalin.Javalin;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

public class AccountController implements Controller{
    private AccountService accountService;
    private UserService userService;

    public AccountController() {
        accountService = new AccountService();
        userService = new UserService();
    }
    @Override
    public void mapEndpoints(Javalin app) {
//        make new account and link to user with a balance of 0
        app.post("/accounts", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String email = (String) session.getAttribute("email");
            User myUser = userService.getUserByEmail(email);
            if (myUser.getUserRole().equals("2")) {
                ObjectMapper om = new ObjectMapper();
                Map<String, String> newAccount = om.readValue(ctx.body(), Map.class);
                try {
                    ctx.json(accountService.openAccount(newAccount));
                    ctx.status(201);
                } catch (InvalidParameterException e) {
                    ctx.json(e.getMessages());
                    ctx.status(400);
                }
            }else{
                ctx.result("You are not logged in as an employee!");
                ctx.status(404);
            }
        });
//               link users to accounts
        app.put("/accounts/{aId}/users/{email}", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String role = (String) session.getAttribute("userRole");
            if (role.equals("2")) {
                String email = ctx.pathParam("email");
                System.out.println(email);
                int aId = Integer.parseInt(ctx.pathParam("aId"));

                try {
                    ctx.json(accountService.linkUserToAccount(aId, email));
                    ctx.status(200);
                } catch (InvalidParameterException e) {
                    ctx.json(e.getMessages());
                    ctx.status(400);
                }
            }
        });
// Unlink account from user
        app.delete("/accounts/{aId}/users/{email}", ctx -> {
           HttpServletRequest req = ctx.req;
           HttpSession session = req.getSession();
           String role = (String) session.getAttribute("userRole");
           if (role.equals("2")){
               String email = ctx.pathParam("email");
               int aId = Integer.parseInt(ctx.pathParam("aId"));
               try {
                   ctx.json(accountService.unlinkUserFromAccount(aId, email));
                   ctx.status(200);
               } catch (InvalidParameterException e) {
                   ctx.json(e.getMessages());
                   ctx.status(400);
               }
           }
        });
// Delete account that is completely unlinked not currently used
        app.delete("/accounts/{aId}", ctx -> {
           HttpServletRequest req = ctx.req;
           HttpSession session = req.getSession();
           String role = (String) session.getAttribute("userRole");
           if (role.equals("2")) {
               try {
                   int aId = Integer.parseInt(ctx.pathParam("aId"));
                   ctx.json(accountService.deleteAccount(aId));
                   ctx.status(200);
               } catch (InvalidParameterException e) {
                   ctx.json(e.getMessages());
                   ctx.status(400);
               }
           }
        });

// get all accounts for an employee or a user with a user id that leads to the same email passed in
        app.get("/{userEmail}/accounts", ctx -> {

            String email = ctx.pathParam("userEmail");
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String role = (String) session.getAttribute("userRole");
            int uId = (Integer) session.getAttribute("userId");
            User myUser = userService.getUserByEmail(email);

            if (Objects.equals(role, "2") || Objects.equals(myUser.getUserId(), uId)) {
                ctx.json(accountService.getAccountsByEmail(email));
                ctx.status(200);
            } else {
                ctx.result("You are not logged in!");
                ctx.status(404);
            }
        });
// gets an account by an account id
        app.get("/accounts/{aId}", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String email = (String) session.getAttribute("email");
            User myUser = userService.getUserByEmail(email);
            int uId = (Integer) session.getAttribute("userId");
            String role = (String) session.getAttribute("userRole");
            int aId = Integer.parseInt(ctx.pathParam("aId"));
            if (Objects.equals(role, "2") || Objects.equals(myUser.getUserId(), uId)) {
                ctx.json(accountService.getAccountByEmailAndAccountId(email, aId));
                ctx.status(200);
            }
            else {
                ctx.result("You are not logged in!");
                ctx.status(404);
            }
        });
//Gets all users by name that are connected to the account id passed in
        app.get("/accounts/{aId}/users", ctx -> {
            HttpServletRequest req = ctx.req;
            HttpSession session = req.getSession();
            String email = (String) session.getAttribute("email");
            User myUser = userService.getUserByEmail(email);
            int aId = Integer.parseInt(ctx.pathParam("aId"));
            int uId = (Integer) session.getAttribute("userId");
            String role = (String) session.getAttribute("userRole");
            if (Objects.equals(role, "2") || Objects.equals(myUser.getUserId(), uId)) {
                ctx.json(accountService.obtainListOfAccountOwners(aId));
                ctx.status(200);
            }
            else {
                ctx.result("You are not logged in!");
                ctx.status(404);
            }
        });
    }
}