package br.com.caioalura.ecommerce;

import br.com.caioalura.ecommerce.ecommerce.CorrelationId;
import br.com.caioalura.ecommerce.ecommerce.KafkaDispatcher;
import br.com.caioalura.models.Email;
import br.com.caioalura.models.Order;
import org.eclipse.jetty.servlet.Source;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {
    private final KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String orderId = UUID.randomUUID().toString();
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));

        Order order = new Order(orderId, amount, email);
        try {
            orderKafkaDispatcher.send("ECOMMERCE_NEW_ORDER", email, new CorrelationId(NewOrderServlet.class.getSimpleName()), order);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Email emailCode = new Email("Caio", "Thank u! We are processing ur order!");
        try {
            emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, new CorrelationId(NewOrderServlet.class.getSimpleName()), emailCode.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("New order sent successfully! :)");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("New order sent successfully! :)");
    }

    @Override
    public void destroy() {
        super.destroy();
        orderKafkaDispatcher.close();
        emailDispatcher.close();
    }
}


