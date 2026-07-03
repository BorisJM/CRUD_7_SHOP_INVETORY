package com.boris.shopinventorycrud.view;

import com.boris.shopinventorycrud.entity.Product;
import com.boris.shopinventorycrud.service.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.Collections;

@Route("")
public class ProductView extends VerticalLayout {

    private final ProductService productService;

    private Product currentEditedProduct;

    private final Grid<Product> grid = new Grid<>(Product.class);

    public ProductView(ProductService productService) {
    this.productService = productService;
        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(e -> {
            Product selectedProduct = grid.asSingleSelect().getValue();
            if (selectedProduct != null) {
                productService.deleteProduct(selectedProduct.getId());
                refreshGrid();
                currentEditedProduct = null;
            }
        });

        TextField nameField = new TextField("Name");
        TextField categoryField = new TextField("Category");
        IntegerField quantityField = new IntegerField("Quantity");
        NumberField priceField = new NumberField("Price");
        ComboBox<Product.Status> statusField = new ComboBox<>("Status");
        statusField.setItems(Product.Status.values());

        Button updateButton = new Button("Update");


        grid.addItemClickListener(e -> {
            currentEditedProduct = e.getItem();
            nameField.setValue(currentEditedProduct.getName());
            categoryField.setValue(currentEditedProduct.getCategory());
            quantityField.setValue(currentEditedProduct.getQuantity());
            priceField.setValue(currentEditedProduct.getPrice());
            statusField.setValue(currentEditedProduct.getStatus());
        });

        updateButton.addClickListener(e -> {
            if (currentEditedProduct != null) {
                Product product = Product.builder()
                        .name(nameField.getValue())
                        .category(categoryField.getValue())
                        .price(priceField.getValue())
                        .quantity(quantityField.getValue())
                        .status(statusField.getValue())
                        .build();
                productService.updateProduct(currentEditedProduct.getId(), product);
                nameField.clear();
                categoryField.clear();
                priceField.clear();
                quantityField.clear();
                statusField.clear();
                currentEditedProduct = null;
                refreshGrid();
            }
        });
        Button saveButton = new Button("Save");

        saveButton.addClickListener(event -> {

            Product product = Product.builder()
                    .name(nameField.getValue())
                    .category(categoryField.getValue())
                    .price(priceField.getValue())
                    .quantity(quantityField.getValue())
                    .status(statusField.getValue())
                    .build();

            productService.createProduct(product);

            refreshGrid();

            nameField.clear();
            categoryField.clear();
            priceField.clear();
            quantityField.clear();
            statusField.clear();
        });


//        Form layout
        FormLayout formLayout = new FormLayout();

        formLayout.add(
          nameField,
          categoryField,
                priceField,
                quantityField,
                statusField
        );

        ComboBox<Product.Status> statusFieldFilter = new ComboBox<>("Status filter");
        statusFieldFilter.setItems(Product.Status.values());
        Button filterButton = new Button("Filter");
        Button showAllButton = new Button("Show all");

      HorizontalLayout buttons = new HorizontalLayout(saveButton, updateButton, deleteButton);

        filterButton.addClickListener(e -> {
            grid.setItems(productService.getAllAvailableProductsByStatus(statusFieldFilter.getValue()));
        });

        showAllButton.addClickListener(e -> {
            grid.setItems(productService.getAllProducts());
            statusFieldFilter.setValue(null);
        });

        Button lowStockButtonFilter = new Button("Low Stock filter");
        lowStockButtonFilter.addClickListener(e -> {
            grid.setItems(productService.getAllLowStockProducts());
        });

//        Min price filter
        NumberField minPriceField = new NumberField("Minimum price");
        Button expensiveButton = new Button("Show expensive");
        expensiveButton.addClickListener(e -> {
            if (minPriceField.getValue() != null) {
                grid.setItems(productService.getProductsWithPriceGreaterThan(minPriceField.getValue()));
            }
        });

//      Category search filter
        TextField categoryFilter = new TextField("Category");
        categoryFilter.addValueChangeListener(e -> {
            grid.setItems(productService.findByCategory(categoryFilter.getValue()));
        });

//      Most expensive product
        Button mostExpensiveButtonFilter = new Button("Most expensive product");
        mostExpensiveButtonFilter.addClickListener(e -> {
            grid.setItems(Collections.singletonList(productService.getMostExpensiveProduct()));
        });


//      Important products button
        Button mostImportantProductsButtonFilter = new Button("Most important products");
        mostImportantProductsButtonFilter.addClickListener(e -> {
            grid.setItems(productService.getProductsOverTwentyStockValue());
        });

        HorizontalLayout filters = new HorizontalLayout(
                statusFieldFilter,
                filterButton,
                showAllButton,
                lowStockButtonFilter,
                minPriceField,
                expensiveButton,
                categoryFilter
        );

//        Grouping sections
        H2 formTitle = new H2("Product form");
        H2 filterTitle = new H2("Filters");
        H2 gridTitle = new H2("Products");



        add(
               formTitle,
                formLayout,
                buttons,
                filterTitle,
                filters,
                gridTitle,
                grid
        );

        refreshGrid();
    }

    private void refreshGrid() {
        grid.setItems(productService.getAllProducts());
    }
}