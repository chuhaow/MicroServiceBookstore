document.addEventListener('alpine:init', ()=>{
    Alpine.data('initData', (pageNo) => ({
        pageNo: pageNo,
        products:{
            data:[]
        },
        init(){
            this.loadProducts(pageNo)
            updateCartItemCount();
        },
        loadProducts(pageNo){
            console.log("loading products")
            $.getJSON("/api/products?page="+pageNo, (response) =>{
                console.log(response)
                this.products=response;
            })
        },
        addToCart(product){
            console.log(product)
            console.log("Adding "+ product.name)
            /**/
            addProductToCart(product)
        }
    }))
});