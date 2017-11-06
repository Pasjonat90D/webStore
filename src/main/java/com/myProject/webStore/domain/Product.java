package com.myProject.webStore.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myProject.webStore.validator.ProductId;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;

@XmlRootElement(name = "product")
public class Product {

    @Pattern(regexp="P[0-9]+", message="{Pattern.Product.productId.validation}")
    @ProductId
    private String productId;
    @Size(min=4, max=50, message="{Size.Product.name.validation}")
    private String name;
    @Min(value=0, message="Min.Product.unitPrice.validation}")
    @Digits(integer=8, fraction=2, message="{Digits.Product.unitPrice.validation}")
    @NotNull(message= "{NotNull.Product.unitPrice.validation}")
    private BigDecimal unitPrice;
    private String description;
    private String manufacturer;
    @NotNull(message= "{NotNull.Product.category.validation}")
    private String category;
    @Min(value=0, message="Min.Product.unitsInStock.validation}")
    private long unitsInStock;
    private long unitsInOrder;
    private boolean discontinued;
    private String condition;
    @JsonIgnore
    private MultipartFile productImage;
    private MultipartFile productPdf;

    public Product() {
        super();
    }

    public Product(String productId, String name, BigDecimal unitPrice) {
        this.productId = productId;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Produkt [productId=" + productId + ", nazwa=" + name + "]";
    }

    public String getProductId() {
        return productId;
    }
    @XmlElement
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }
    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    @XmlElement
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }
    @XmlElement
    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }
    @XmlElement
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }
    @XmlElement
    public void setCategory(String category) {
        this.category = category;
    }

    public long getUnitsInStock() {
        return unitsInStock;
    }
    @XmlElement
    public void setUnitsInStock(long unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public long getUnitsInOrder() {
        return unitsInOrder;
    }
    @XmlElement
    public void setUnitsInOrder(long unitsInOrder) {
        this.unitsInOrder = unitsInOrder;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }
    @XmlElement
    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }

    public String getCondition() {
        return condition;
    }
    @XmlElement
    public void setCondition(String condition) {
        this.condition = condition;
    }
    @XmlTransient
    public MultipartFile getProductImage() {
        return productImage;
    }

    public void setProductImage(MultipartFile productImage) {
        this.productImage = productImage;
    }

    public MultipartFile getProductPdf() {
        return productPdf;
    }

    public void setProductPdf(MultipartFile productPdf) {
        this.productPdf = productPdf;
    }
}