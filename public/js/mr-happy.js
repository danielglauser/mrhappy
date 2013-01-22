$(document).ready(function() {
  $('.controls > #sad').click(function() {
    $('body').removeClass('neutral');
    $('body').removeClass('smile');
    $('body').addClass('frown');

    $('.eye').removeClass('neutral');
    $('.eye').removeClass('smile');
    $('.eye').addClass('frown');

    $('.mouth').removeClass('neutral');
    $('.mouth').removeClass('smile');
    $('.mouth').addClass('frown');

    $('.mouth > h1').removeClass('neutral');
    $('.mouth > h1').removeClass('smile');
    $('.mouth > h1').addClass('frown');
  });

  $('.controls > #neutral').click(function() {
    $('body').removeClass('frown');
    $('body').removeClass('smile');
    $('body').addClass('neutral');

    $('.eye').removeClass('frown');
    $('.eye').removeClass('smile');
    $('.eye').addClass('neutral');

    $('.mouth').removeClass('frown');
    $('.mouth').removeClass('smile');
    $('.mouth').addClass('neutral');

    $('.mouth > h1').removeClass('frown');
    $('.mouth > h1').removeClass('smile');
    $('.mouth > h1').addClass('neutral');
  });

  $('.controls > #happy').click(function() {
    $('body').removeClass('frown');
    $('body').removeClass('neutral');
    $('body').addClass('smile');

    $('.eye').removeClass('frown');
    $('.eye').removeClass('neutral');
    $('.eye').addClass('smile');

    $('.mouth').removeClass('frown');
    $('.mouth').removeClass('neutral');
    $('.mouth').addClass('smile');

    $('.mouth > h1').removeClass('frown');
    $('.mouth > h1').removeClass('neutral');
    $('.mouth > h1').addClass('smile');
  });
});
