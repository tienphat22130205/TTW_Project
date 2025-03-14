package vn.edu.hcmuaf.fit.project_fruit.service;

import vn.edu.hcmuaf.fit.project_fruit.dao.InvoiceDao;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Customer;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.DailyRevenue;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Invoice;
import vn.edu.hcmuaf.fit.project_fruit.dao.model.Product;

import java.util.List;

public class InvoiceService {
    private final InvoiceDao invoiceDao;

    public InvoiceService() {
        this.invoiceDao = new InvoiceDao();
    }

    public List<Invoice> getInvoicesByPage(int page, int recordsPerPage) {
        return invoiceDao.getInvoicesByPage(page, recordsPerPage);
    }

    public int getTotalRecords() {
        return invoiceDao.getTotalRecords();
    }
    public List<Invoice> getAllInvoices() {
        return invoiceDao.getAllInvoices(); // Gọi phương thức từ DAO
    }

}
