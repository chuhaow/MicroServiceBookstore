document.addEventListener('alpine:init', ()=>{
    Alpine.data('initData', (pageNo) => ({
        isAuthenticated: false,
        pageNo: pageNo,
        products:{
            data:[]
        },
        init(){
            const authStatusElement = document.getElementById('auth-status');
            this.isAuthenticated = authStatusElement !== null
            this.loadProducts(pageNo)
            generateGuestId()
            if(this.isAuthenticated){
                updateCartItemCount();
            }else{
                guestUpdateCartItemCount()
            }
        },
        loadProducts(pageNo){
            console.log("loading products")
            $.getJSON("/api/products?page="+pageNo, (response) =>{
                console.log(response)
                this.products=response;
            })
        },
        addToCart(product){
            console.log("Adding "+ product.name)
            addProductToCart(product)
        },
        addToGuestCart(product){
            console.log("Adding "+ product.name)
            guestAddProductToCart(product)
        }
    }))
});