<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">

    <script src="/js/controllers.js"></script>
    <title>Produkt</title>

<section>

</section>
<section class="container">
    <div class="row">
        <div class="col-md-5">
            <img src="<c:url value="/images/${product.productId}.jpeg"></c:url>"alt="image" style = "width:100%"/>
        </div>
        <div class="col-md-5">
            <h3>${product.name}</h3>
            <p>${product.description}</p>
            <p>
                <strong>Kod produktu: </strong><span class="label label-warning">${product.productId}</span>
            </p>
            <p>
                <strong>
                    <spring:message code="product.manufacturer.label"/>
                </strong>: ${product.manufacturer}
            </p>
            <p>
                <strong>Kategoria</strong>: ${product.category}
            </p>
            <p>
                <strong>Dostępna liczba sztuk</strong>:${product.unitsInStock}
            </p>
            <h4>${product.unitPrice}PLN</h4>
            <p ng-controller="cartCtrl">
                <a href="<spring:url value="/products" />" class="btn btndefault">
                    <span class="glyphicon-hand-left glyphicon"></span> Wstecz
                </a>
                <section class="container" ng-app="cartApp">
                <a href="#" class="btn btn-warning btn-large" ngclick="addToCart('${product.productId}')">
                    <span class="glyphicon-shopping-cart glyphicon"></span>
                    Zamów teraz
                </a>
                </section>
                <a href="<spring:url value="/cart" />" class="btn btn-default">
                    <span class="glyphicon-hand-right glyphicon"></span> Koszyk
                </a>
            </p>
        </div>
    </div>
</section>
