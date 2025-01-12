const params = new URLSearchParams(window.location.search);
const merge = params.get('merge');

if(merge){
    $.ajax({
        url: '/api/carts/merge/' + getGuestId(),
        type: "POST",
        success: (resp) =>{
            updateCartItemCount()
        },
        error:(err) =>{
            console.log("Error merging cart: ", err)
        }
    })
}