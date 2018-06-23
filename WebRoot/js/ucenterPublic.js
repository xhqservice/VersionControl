/* placeholder */
var JPlaceHolder = {
    //检测
    _check : function(){
        return 'placeholder' in document.createElement('input');
    },
    init : function(){
        if(!this._check()){
            this.fix();
        }
    },
    fix : function(){
        jQuery(':input[placeholder]').each(function(index, element) {
            var self = $(this), txt = self.attr('placeholder');
            self.wrap($('<div></div>').css({position:'relative', zoom:'1', display:'inline-block', border:'none', background:'none', padding:'none', margin:'none'}));
            var pos = self.position(), h = self.outerHeight(true), paddingleft = self.css('padding-left');
            var holder = $('<span></span>').text(txt).css({position:'absolute', left:pos.left, top:pos.top, height:h, lineHeight:h+'px', paddingLeft:paddingleft, color:'#aaa'}).appendTo(self.parent());
            self.focusin(function(e) {
                holder.hide();
            }).focusout(function(e) {
                if(!self.val()){
                    holder.show();
                }
            });
            holder.click(function(e) {
                holder.hide();
                self.focus();
            });
        });
    }
};

jQuery(function(){
    JPlaceHolder.init();
});

$('.moneyListDiv:last').css('border','none');

/* header right nav */
$('.headerLi1').mouseenter(function(){
	$('.centerLogout').slideDown();
}).mouseleave(function(){
	$('.centerLogout').stop(true).slideUp();
});

$('.headerLi3').mouseenter(function(){
	$('.juheCode').slideDown();
}).mouseleave(function(){
	$('.juheCode').stop(true).slideUp();
});


//nav sub
$('.ucenterParent').each(function(){
    $(this).click(function(){
        $(this).next('.ucenterSub').stop(true).slideToggle();
    })
})


// 弹层
function code_num(htmlCode){
  layer.open({
    type: 1,
    shadeClose: true,
    title: '笑话大全预警设置',
    scrollbar: true,
    area: ['auto', 'auto'],
    shade: [0.6],
    skin: 'layui-layer-nobg',
    closeBtn: 0,
    content: htmlCode
  });
}

function isEmail(email){
   var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
   if(!myreg.test(email)) return false;
   return true;
}
function isPhone(phone){
    var mreg =/^1[3|5|8|4|7]{1}[0-9]{1}[0-9]{8}$/;
    if(!mreg.test(phone)){
        return false;
    }
    return true;
}
//判断是否为正整数
function isNumber(num){
  var reg = /^\d+(?=\.{0,1}\d+$|$)/
  if(reg.test(num)) return true;
  return false ;
}
