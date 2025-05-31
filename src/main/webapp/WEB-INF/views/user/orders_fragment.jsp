<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "f" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<style>
  table.order-table {
    width: 100%;
    border-collapse: collapse;
    font-family: Arial, sans-serif;
  }

  table.order-table th, table.order-table td {
    border: 1px solid #ddd;
    padding: 12px 15px;
    text-align: center;
    vertical-align: middle;
    font-size: 11px;
  }

  table.order-table th {
    background-color: #f8f8f8;
    font-weight: 600;
    color: #333;
    font-size: 13px;
  }

  table.order-table tr:nth-child(even) {
    background-color: #fafafa;
  }

  table.order-table tr:hover {
    background-color: #f1f1f1;
  }

  .product-image {
    width: 60px;
    height: 60px;
    border: 1px solid #ccc;
    padding: 3px;
    object-fit: contain;
    border-radius: 4px;
  }

  .action-button {
    background-color: #007bff;
    border: none;
    color: white;
    padding: 6px 12px;
    margin-right: 6px;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
    transition: background-color 0.2s ease-in-out;
  }

  .action-button:hover {
    background-color: #0056b3;
  }

  .status-text {
    font-style: italic;
    color: #4CAF50;
    font-weight: 600;
  }
</style>

<table class="order-table">
  <thead>
  <tr>
    <th>Đơn hàng</th>
    <th>Sản phẩm</th>
    <th>Số lượng</th>
    <th>Giá</th>
    <th>Hành động</th>
  </tr>
  </thead>
  <tbody>
  <c:choose>
    <c:when test="${empty orders}">
      <tr>
        <td colspan="5" style="text-align: center; padding: 20px;">
          Quý khách chưa có đơn hàng nào.
        </td>
      </tr>
    </c:when>
    <c:otherwise>
      <c:forEach var="order" items="${orders}">
        <tr>
          <td>
            <strong>#${order.invoiceId}</strong><br/>
            <small><fmt:formatDate value="${order.createDate}" pattern="dd-MM-yyyy"/></small><br/>
            <span class="status-text">Trạng thái: Đã thanh toán</span>
          </td>
          <td>
            <img src="${order.imageUrl}" alt="${order.productName}" class="product-image" /><br/>
              ${order.productName}
          </td>
          <td style="text-align: center;">${order.quantity}</td>
          <td style="text-align: right;">
            <fmt:formatNumber value="${order.unitPrice}" type="currency" currencySymbol="₫"/>
          </td>
          <td style="text-align: center;">
            <form action="${pageContext.request.contextPath}/product-detail" method="get" style="display:inline;">
              <input type="hidden" name="pid" value="${order.productId}" />
              <button class="action-button" type="submit">Mua lại</button>
            </form>
            <form action="${pageContext.request.contextPath}/product-detail" method="get" style="display:inline;">
              <input type="hidden" name="pid" value="${order.productId}" />
              <button class="action-button" type="submit">Đánh giá</button>
            </form>
          </td>
        </tr>
      </c:forEach>
    </c:otherwise>
  </c:choose>
  </tbody>
</table>
