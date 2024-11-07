document.addEventListener('alpine:init', ()=>{
    Alpine.data('initData', (pageNo) => ({
        pageNo: pageNo,
        products:{
            data:[]
        },
        init(){
            this.loadProducts(pageNo)
        },
        loadProducts(pageNo){
            console.log("loading products")
            $.getJSON("http://localhost:8989/catalog/api/products?page="+pageNo, (response) =>{
                console.log(response)
                this.products=response;
            })
        },
        addToCart(product){
            console.log("Adding "+ product.name)
            addProductToCart(product)
        }
    }))
});