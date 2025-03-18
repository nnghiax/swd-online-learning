/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function updateQuantity(button, change) {
    var input = $(button).closest('.quantity').find('input');
    var oldValue = parseInt(input.val());
    var instock = parseInt(input.data('instock'));
    var newVal = oldValue + change;
    if (newVal < 1) {
        newVal = 1;
    } else if (newVal > instock) {
        alert("Cannot add more than the available stock.")
        newVal = instock;
    }

    input.val(newVal);

    var row = $(button).closest('tr');
    var productId = row.data('product-id');
    var price = parseFloat(row.find('.price').text());
    var total = price * newVal;
    row.find('.total').text(total.toFixed(1));
    updateCartSummary();
    updateCartOnServer(productId, newVal);
}

function updateCartOnServer(productId, quantity) {
    $.ajax({
        url: 'updateCart',
        type: 'POST',
        data: {
            productId: productId,
            quantity: quantity
        },
        success: function(response) {
            if (response === "success") {
                // Optionally reload or redirect
//                window.location.href = 'checkout.jsp';
            } else {
                console.error('Error updating cart:', response);
            }
        },
        error: function(error) {
            console.error('Error updating cart:', error);
        }
    });
}

function updateCartSummary() {
    var totalPrice = 0;
    $('.total').each(function () {
        totalPrice += parseFloat($(this).text());
    });

    $('#cart-subtotal').text(totalPrice.toFixed(1));

    var shippingFee = $('tbody tr').length > 1 ? 10 : 0;
    $('#cart-shipping').text(shippingFee.toFixed(1));

    var total = totalPrice + shippingFee;
    $('#cart-total').text(total.toFixed(1));
}

$(document).ready(function () {
    $('.btn-plus, .btn-minus').off('click').on('click', function () {
        var button = $(this);
        var change = button.hasClass('btn-plus') ? 1 : -1;
        updateQuantity(button[0], change);
    });

    updateCartSummary();
});
