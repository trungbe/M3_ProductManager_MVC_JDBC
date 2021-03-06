package controller;

import model.Product;
import service.IProductService;
import service.ProductService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", urlPatterns = "/products")
public class ProductServlet extends HttpServlet {
    private static final IProductService productService = new ProductService();

    //------------------------------doGet
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create": {
                showCreateForm(request, response);
                break;
            }
            case "edit": {
                showUpdateForm(request, response);
                break;
            }
            case "delete": {
                showDeleteForm(request, response);
                break;
            }
            case "detail": {
                break;
            }
            case "search": {
                findByName(request, response);
                break;
            }
            case "sort":
                sortByName(request, response, productService.sort());
                break;
            default: {
                showAllProducts(request, response);
                break;
            }
        }
    }

    private void sortByName(HttpServletRequest request, HttpServletResponse response, List<Product> product) {
        request.setAttribute("products", product);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("product/listProduct.jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void findByName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String name = request.getParameter("findName");
//        List<Product> products = new ArrayList<Product>();
//        List<Product> productList = productService.findAll();
//        int check = 0;
//        for (Product product : productList) {
//            if (product.getName().contains(name)) {
//                products.add(product);
//                check = 1;
//            }
//        }
//        if (check == 1) {
//            request.setAttribute("findByName", products);
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher("product/find.jsp");
//            requestDispatcher.forward(request, response);
//        } else {
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher("error-404.jsp");
//            requestDispatcher.forward(request, response);
//        }
//    }

    private void findByName(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("findName");
        List<Product> product = productService.findByName(name);
        RequestDispatcher dispatcher;
        request.setAttribute("products", product);
        dispatcher = request.getRequestDispatcher("product/listProduct.jsp");
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAllProducts(HttpServletRequest request, HttpServletResponse response) {
        List<Product> productList = productService.findAll();
        request.setAttribute("products", productList); //name = items
        RequestDispatcher dispatcher = request.getRequestDispatcher("product/listProduct.jsp");
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showDeleteForm(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productService.findById(id);
        RequestDispatcher dispatcher;
        if (product == null) {
            dispatcher = request.getRequestDispatcher("error-404.jsp");
        } else {
            request.setAttribute("product", product);
            dispatcher = request.getRequestDispatcher("product/delete.jsp");
        }
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productService.findById(id);
        RequestDispatcher dispatcher;
        if (product == null) {
            dispatcher = request.getRequestDispatcher("error-404.jsp");
        } else {
            request.setAttribute("product", product);
            dispatcher = request.getRequestDispatcher("product/update.jsp");
        }
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("product/create.jsp");
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //------------------- doPost
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create": {
                createProduct(request, response);
                break;
            }
            case "edit": {
                updateProduct(request, response);
                break;
            }
            case "delete": {
                deleteProduct(request, response);
                break;
            }

        }
    }


    private void createProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int price = Integer.parseInt(request.getParameter("price"));
        String description = request.getParameter("description");
        String producer = request.getParameter("producer");
        Product product = new Product(id, name, price, description, producer);
        productService.create(product);
        RequestDispatcher dispatcher = request.getRequestDispatcher("product/create.jsp");
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void updateProduct(HttpServletRequest request, HttpServletResponse response) {
//        int id = Integer.parseInt(request.getParameter("id"));
//        int price = Integer.parseInt(request.getParameter("price"));
//        String description = request.getParameter("description");
//        String producer = request.getParameter("producer");
//        String name = request.getParameter("name");
//        Product product = new Product(id, name, price, description, producer);
//        productService.updateById(id, product);
//        RequestDispatcher dispatcher = request.getRequestDispatcher("product/update.jsp");
//        try {
//            dispatcher.forward(request, response);
//        } catch (ServletException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int price = Integer.parseInt(request.getParameter("price"));
        String description = request.getParameter("description");
        String producer = request.getParameter("producer");
        productService.update(new Product(id, name, price, description, producer));
        response.sendRedirect("/products");
//        RequestDispatcher requestDispatcher = request.getRequestDispatcher("product/update.jsp");
//        request.setAttribute("product",p);
//        requestDispatcher.forward(request,response);

    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        productService.delete(id);
        response.sendRedirect("/products");
    }
}
