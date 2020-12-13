
var globalBlogMap = [];

function setCookie(key,value,t){
    var oDate=new Date();
    oDate.setDate(oDate.getDate()+t);
    document.cookie=key+"="+value+"; expires="+oDate.toDateString();
}

//userpage.html中用于append到页面的页码部分
var globalPageInfoAppend = "<div style=\"display: inline-block;float: left\" >\n" +
    "                            <ul class=\"pagination\" >\n" +
    "                                <li><a value=\"last\" class=\"lastPage\">&laquo;</a></li>\n" +
    "                            </ul>\n" +
    "                            <ul class=\"pagination\" id=\"pageHtml\">\n" +
    "                                <!--动态加载进来-->\n" +
    "                            </ul>\n" +
    "                            <ul class=\"pagination\">\n" +
    "                                <li><a value=\"next\" class=\"nextPage\">&raquo;</a></li>\n" +
    "                            </ul>\n" +
    "                            <div id=\"totalPage\" style=\"display: inline-block;\"></div>\n" +
    "                            <div id=\"presentPage\" style=\"display: inline-block;\"></div>\n" +
    "                        </div>";

function toBlogDetail(msg) {
    // console.log(msg);  //msg === this
    var blogContent = msg.parentNode.childNodes[5].innerText;
    var blogTitle = msg.value;
    localStorage.setItem("blogTitle", blogTitle);
    localStorage.setItem("blogContent",blogContent);
    window.location.href = "/blogdetail";
}