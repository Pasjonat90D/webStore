package com.myProject.webStore.domain.repository.impl;

import java.util.HashMap;
import java.util.Map;

import com.myProject.webStore.domain.Cart;
import com.myProject.webStore.domain.repository.CartRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCartRepository implements CartRepository {
    private Map<String, Cart> listOfCarts;
    public InMemoryCartRepository() {
        listOfCarts = new HashMap<String,Cart>();
    }
    public Cart create(Cart cart) {
        if(listOfCarts.keySet().contains(cart.getCartId())) {
            throw new IllegalArgumentException(String.format("Nie można utworzyć koszyka. Koszyk o wskazanym id (%) już  istnieje.",cart.getCartId()));
        }
        listOfCarts.put(cart.getCartId(), cart);
        return cart;
    }
    public Cart read(String cartId) {
        return listOfCarts.get(cartId);
    }
    public void update(String cartId, Cart cart) {
        if(!listOfCarts.keySet().contains(cartId)) {
            throw new IllegalArgumentException(String.format("Nie można zaktualizować koszyka. Koszyk o wskazanym id (%) nie istnieje.",cartId));
        }
        listOfCarts.put(cartId, cart);
    }
    public void delete(String cartId) {
        if(!listOfCarts.keySet().contains(cartId)) {
            throw new IllegalArgumentException(String.format("Nie można usunąć koszyka. Koszyk o wskazanym id (%) nie istnieje.",cartId));
        }
        listOfCarts.remove(cartId);
    }
}