/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
$(document).ready(function() {
    //Lấy tỉnh thành
    $.getJSON('https://esgoo.net/api-tinhthanh/1/0.htm', function(data_tinh) {	       
        if (data_tinh.error == 0) {
            $.each(data_tinh.data, function(key_tinh, val_tinh) {
                $("#city").append('<option value="'+val_tinh.id+'" data-name="'+val_tinh.full_name_en+'">'+val_tinh.full_name_en+'</option>');
            });
            $("#city").change(function(e) {
                var idtinh = $(this).val();
                var nameTinh = $("#city option:selected").data('name');
                $("#cityName").val(nameTinh);  // Store the selected city name

                //Lấy quận huyện
                $.getJSON('https://esgoo.net/api-tinhthanh/2/'+idtinh+'.htm', function(data_quan) {	       
                    if (data_quan.error == 0) {
                        $("#district").html('<option value="0">Select District</option>');  
                        $("#ward").html('<option value="0">Select Ward</option>');   
                        $.each(data_quan.data, function(key_quan, val_quan) {
                            $("#district").append('<option value="'+val_quan.id+'" data-name="'+val_quan.full_name_en+'">'+val_quan.full_name_en+'</option>');
                        });

                        //Lấy phường xã  
                        $("#district").change(function(e) {
                            var idquan = $(this).val();
                            var nameQuan = $("#district option:selected").data('name');
                            $("#districtName").val(nameQuan);  // Store the selected district name

                            $.getJSON('https://esgoo.net/api-tinhthanh/3/'+idquan+'.htm', function(data_phuong) {	       
                                if (data_phuong.error == 0) {
                                    $("#ward").html('<option value="0">Select Ward</option>');   
                                    $.each(data_phuong.data, function(key_phuong, val_phuong) {
                                        $("#ward").append('<option value="'+val_phuong.id+'" data-name="'+val_phuong.full_name_en+'">'+val_phuong.full_name_en+'</option>');
                                    });

                                    $("#ward").change(function(e) {
                                        var namePhuong = $("#ward option:selected").data('name');
                                        $("#wardName").val(namePhuong);  // Store the selected ward name
                                    });
                                }
                            });
                        });
                    }
                });
            });   
        }
    });
});  
$(document).ready(function () {
    $('#city').change(function () {
        var city = $(this).val();
        var shippingFee = 0;

        // Parse totalPrice from hidden input
        var totalPriceStr = $('#hiddenTotalPrice').val();
        var totalPrice = parseFloat(totalPriceStr);

        console.log('Parsed totalPrice:', totalPrice); // Log parsed totalPrice to console for debugging

        if (city === 'Ha Noi City') {
            $('#shippingFee').text('Free Shipping');
        } else {
            shippingFee = 10;
            $('#shippingFee').text('$' + shippingFee);
        }
        // Calculate total amount
        var totalAmount = totalPrice + shippingFee;
        $('#totalAmount').text('$' + totalAmount.toFixed(1)); // Display totalAmount with 2 decimal places

        // Update hidden inputs
        $('#hiddenShippingFee').val(shippingFee);
        $('#hiddenTotalPrice').val(totalAmount.toFixed(1)); // Set hiddenTotalPrice with 2 decimal places
    });
});

