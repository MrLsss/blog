/*-------------------------------------------------------------------------------------------------------------------------------*/

$(function() {


	"use strict";
    
	/*================*/
	/* 01 - VARIABLES */
	/*================*/
    var swipers = [], winW, winH, winScr, footerTop, _isresponsive, _ismobile = navigator.userAgent.match(/Android/i) || navigator.userAgent.match(/webOS/i) || navigator.userAgent.match(/iPhone/i) || navigator.userAgent.match(/iPad/i) || navigator.userAgent.match(/iPod/i);


	/*========================*/
	/* 02 - page calculations */
	/*========================*/
	function pageCalculations(){
		winW = $(window).width();
		winH = $(window).height();
		if($('.is-mobile').is(':visible')) _isresponsive = true;
		else _isresponsive = false;
        $('#content-wrapper').height($(window).height());
        //$('#content-wrapper').height($('#content-wrapper').height());
        //$('body').addClass('hide-h');
	}

	/*=================================*/
	/* 03 - function on document ready */
	/*=================================*/
	pageCalculations();

	//center all images inside containers
	$('.center-image').each(function(){
		var bgSrc = $(this).attr('src');
		$(this).parent().css({'background-image':'url('+bgSrc+')'});
		$(this).hide();
	});

    
	/*============================*/
	/* 04 - function on page load */
	/*============================*/
	$(window).load(function(){
		
		
		$('#loader-wrapper').fadeOut(300);
        $('.company-thumbs').addClass('active');
        
        initSwiper();

        
        if($('.SlectBox').length>0){
            $('.SlectBox').SumoSelect({
                floatWidth: 0
            });
        }

        if($('.grid').length > 0){
                var $grid = $('.grid').isotope({
                  itemSelector: '.grid-item',
                  masonry: {
                    columnWidth: '.grid-sizer'
                  },
                  getSortData: {
                    popular: '[data-popular] parseInt',
                    rate: '[data-rate] parseInt'
                  }
                });

                $('.category li').on('click',function(){
                    if($(this).hasClass('active')) return false;
                    $(this).parent().find('.active').removeClass('active');
                    $(this).addClass('active');
                    var filterValue = $(this).attr('data-filter');
                    $grid.isotope({ filter: filterValue });
                    // $('.filter-title').removeClass('active').text($(this).text());
                    return false;
                });

                $('.sort-by li').on('click',function(){
                    if($(this).hasClass('active')) return false;
                    $(this).parent().find('.active').removeClass('active');
                    $(this).addClass('active');
                    var sortByValue = $(this).attr('data-sort-value');
                    $grid.isotope({ sortBy: sortByValue });
                    // $('.filter-title').removeClass('active').text($(this).text());
                    return false;
                });
            };

        if(($('.skrollr-item').length > 0)&&(!_isresponsive)){
            var s = skrollr.init();
        }

        if($('.wow').length>0){
            new WOW().init();
        }
        

	});


	/*==============================*/
	/* 05 - function on page resize */
	/*==============================*/
	function resizeCall(){
		pageCalculations();
		$('.swiper-container.initialized[data-slides-per-view="responsive"]').each(function(){
			var thisSwiper = swipers['swiper-'+$(this).attr('id')], $t = $(this), slidesPerViewVar = updateSlidesPerView($t);
			thisSwiper.params.slidesPerView = slidesPerViewVar;
			thisSwiper.reInit();
			var paginationSpan = $t.find('.pagination span');
			paginationSpan.hide().slice(0,(paginationSpan.length+1-slidesPerViewVar)).show();
		});
	}
	$(window).resize(function(){
		resizeCall();
	});
	window.addEventListener("orientationchange", function() {
		resizeCall();
	}, false);


	/*==============================*/
	/* 06 - function on page scroll */
	/*==============================*/
	var _buffer = null;
	$('#content-wrapper').scroll(function(){
        scrollCall();
	});
    
	function scrollCall(){
		var winScroll = $('#content-wrapper').scrollTop();

        if($('#content-wrapper').scrollTop()>=25){
            $('header').addClass('moove'); 
        }else{
            $('header').removeClass('moove');    
        }
        
        if(stop_scroll_ajax == 1){
            $('#content-wrapper').scrollTop(0);
        }
	}

	/*=====================*/
	/* 07 - swiper sliders */
	/*=====================*/
    function initSwiper(){
        var initIterator = 0;
        $('.swiper-container').each(function(){                               
            var $t = $(this);                                 

            var index = 'swiper-unique-id-'+initIterator;

            $t.addClass('swiper-'+index+' initialized').attr('id', index);
            $t.find('.swiper-pagination').addClass('swiper-pagination-'+index);
            $t.find('.swiper-button-prev').addClass('swiper-button-prev-'+index);
            $t.find('.swiper-button-next').addClass('swiper-button-next-'+index);
            if($t.find('.swiper-slide').length<=1) $('.slider-click[data-pagination-rel="'+$t.data('pagination-rel')+'"]').addClass('disabled');

            var slidesPerViewVar = ($t.data('slides-per-view'))?$t.data('slides-per-view'):1,
                loopVar = ($t.data('loop'))?parseInt($t.data('loop'), 10):0;
            if(slidesPerViewVar!='auto') slidesPerViewVar = parseInt(slidesPerViewVar, 10);

            swipers['swiper-'+index] = new Swiper('.swiper-'+index,{
                pagination: '.swiper-pagination-'+index,
                paginationClickable: true,
                nextButton: '.swiper-button-next-'+index,
                prevButton: '.swiper-button-prev-'+index,
                slidesPerView: slidesPerViewVar,
                spaceBetween: ($t.data('space'))?parseInt($t.data('space'), 10):0,
                autoHeight: ($t.data('auto-height'))?parseInt($t.data('auto-height'), 10):0,
                loop: loopVar,
                autoplay: ($t.data('autoplay'))?parseInt($t.data('autoplay'), 10):0,
                centeredSlides: ($t.data('center'))?parseInt($t.data('center'), 10):0,
                breakpoints: ($t.data('breakpoints'))? { 767: { slidesPerView: parseInt($t.attr('data-xs-slides'), 10) }, 991: { slidesPerView: parseInt($t.attr('data-sm-slides'), 10) }, 1199: { slidesPerView: parseInt($t.attr('data-md-slides'), 10) }, 1599: { slidesPerView: parseInt($t.attr('data-lg-slides'), 10) } } : {},
                initialSlide: ($t.data('ini'))?parseInt($t.data('ini'), 10):0,
                watchSlidesProgress: true,
                speed: ($t.data('speed'))?parseInt($t.data('speed'), 10):500,
                parallax: ($t.data('parallax'))?parseInt($t.data('parallax'), 10):0,
                slideToClickedSlide: false,
                keyboardControl: true,
                mousewheelControl: ($t.data('mousewheel'))?parseInt($t.data('mousewheel'), 10):0,
                mousewheelReleaseOnEdges: true,
                direction: ($t.data('direction'))?$t.data('direction'):'horizontal',
                onProgress: function(swiper, progress){
                    watchSwiperProgress($t,swiper,swiper.activeIndex);
                },
                // onClick: function(swiper){
                    // var imgSrc = $(this).data('img-src');
                    // $('.swipers-img img').attr('src', imgSrc);
                // },
                onSlideChangeStart: function(swiper){
                    var activeIndex = (loopVar===1)?swiper.activeLoopIndex:swiper.activeIndex;
                    watchSwiperProgress($t,swiper,activeIndex);
                },
                onTransitionEnd: function(swiper){
                    var activeIndex = (loopVar===1)?swiper.activeLoopIndex:swiper.activeIndex;
                    if($('.slider-click[data-pagination-rel="'+$t.data('pagination-rel')+'"]').length){
                        var updateLeftNum = $('.slider-click.left[data-pagination-rel="'+$t.data('pagination-rel')+'"]'),
                            updateRightNum = $('.slider-click.right[data-pagination-rel="'+$t.data('pagination-rel')+'"]');
                        if(loopVar!=1){
                            if(activeIndex<1) updateLeftNum.addClass('disabled');
                            else updateLeftNum.removeClass('disabled').find('.left').text(activeIndex);
                            if(activeIndex+2>swiper.slides.length) updateRightNum.addClass('disabled');
                            else updateRightNum.removeClass('disabled').find('.left').text(activeIndex+2);
                            updateLeftNum.find('.preview-entry.active').removeClass('active');
                            updateLeftNum.find('.preview-entry[data-rel="'+(activeIndex-1)+'"]').addClass('active');
                            updateRightNum.find('.preview-entry.active').removeClass('active');
                            updateRightNum.find('.preview-entry[data-rel="'+(activeIndex+1)+'"]').addClass('active');
                        }
                    }
                    //slider number change
                    $('.prev-slide').text(activeIndex);
                    if(activeIndex===0){
                        $('.prev-slide').text(1);
                    }
                    $('.next-slide').text(activeIndex+2);
                    if(activeIndex===$('.left-right .swiper-container .swiper-slide').length-1){
                        $('.next-slide').text(activeIndex+1);
                    }
                }
            });
            swipers['swiper-'+index].update();
            initIterator++;
        });

    }

    function watchSwiperProgress(container, swiper, index){
        if($('.homepage-1-backgrounds[data-pagination-rel="'+container.data('pagination-rel')+'"]').length){
            $('.homepage-1-backgrounds .entry.active').removeClass('active');
            $('.homepage-1-backgrounds .entry[data-rel='+index+']').addClass('active');
        }
        if($('.slider-click-label[data-pagination-rel="'+container.data('pagination-rel')+'"]').length){
            $('.slider-click-label[data-pagination-rel="'+container.data('pagination-rel')+'"]').removeClass('active prev next');
            $('.slider-click-label[data-pagination-rel="'+container.data('pagination-rel')+'"][data-slide-to="'+index+'"]').addClass('active');
        }
        if($('.pagination-slider-wrapper[data-pagination-rel="'+container.data('pagination-rel')+'"]').length){
            var foo = $('.pagination-slider-wrapper[data-pagination-rel="'+container.data('pagination-rel')+'"]');
            foo.css({'top':(-1)*parseInt(foo.find('.active').attr('data-slide-to'), 10)*foo.parent().height()});
        }        
    }

    var slide_index = 1;
    $('.all-slides').text($('.left-right .swiper-container.my-bg-swiper .swiper-slide').length);
    $('.prev-slide').text(slide_index);
    $('.next-slide').text(slide_index+1);

    $('.slider-click.left').on('click', function(){
        if($(this)[0].hasAttribute('data-pagination-rel')){
            swipers['swiper-'+$('.swiper-container[data-pagination-rel="'+$(this).data('pagination-rel')+'"]').attr('id')].slidePrev();
            $(this).find('.preview-entry').removeClass('active');
        }
    });

    $('.slider-click.right').on('click', function(){
        if($(this)[0].hasAttribute('data-pagination-rel')){
            swipers['swiper-'+$('.swiper-container[data-pagination-rel="'+$(this).data('pagination-rel')+'"]').attr('id')].slideNext();
            $(this).find('.preview-entry').removeClass('active');
        }
    });

    $('.slider-click-label').on('click', function(){
        swipers['swiper-'+$('.swiper-container[data-pagination-rel="'+$(this).data('pagination-rel')+'"]').attr('id')].slideTo($(this).data('slide-to'));
    });


	/*==============================*/
	/* 08 - buttons, clicks, hovers */
	/*==============================*/
    //open overlay popup
    $('.open-overlay').on('click', function(){
        var $this = $(this);
        $('.overlay[data-rel="'+$(this).data('rel')+'"]').addClass('active');
        if($(this).hasClass('open-video')) setTimeout(function(){$('.overlay[data-rel="'+$this.data('rel')+'"] iframe').attr('src', $this.data('src'));}, 500);
        if(_ismobile) setTimeout(function(){$('html').addClass('overflow-hidden');}, 500);
    });
    //close overlay popup
    $('.submit.overlay .button-close').on('click', function(){
        $(this).closest('.video-popup').find('iframe').attr('src', '');
        if($('.overlay.active').length===1) $('html').removeClass('overflow-hidden');
        $(this).closest('.overlay').removeClass('active');
    });

    $('.moove-icon').on('click', function(){
        if($('header').hasClass('active')){
            $('header').removeClass('active');
            if(_isresponsive){
                $('body,html').removeClass('stopscroll');
            }
        }else{
            $('header').addClass('active');
            if(_isresponsive){
                $('body,html').addClass('stopscroll');
            }
        }
        return false;
    });
    
    $('.mob-icon').click(function(){
        if($('.mob-icon').hasClass('active')){
            $('.mob-icon').removeClass('active1');  
            setTimeout(function(){$('.mob-icon').removeClass('active');},250);
            $('body').removeClass('stopscroll');
            $('html').removeClass('stopscroll');
            $('nav').removeClass('active');
            $('header').removeClass('active');
        }else{
            $('.mob-icon').addClass('active');  
            setTimeout(function(){$('.mob-icon').addClass('active1');},250);
            $('body').addClass('stopscroll');
            $('html').addClass('stopscroll');
            $('nav').addClass('active');
            $('header').addClass('active');
        }
        return false;
    });
    
    $('.content-meny-select').click(function(){
        if($(this).next().hasClass('active')){
            $(this).next().removeClass('active');    
        }else{
            $(this).next().addClass('active');
        }
        return false;
    });
    
    
    $('.search').click(function(){
        $('.searh-pop').addClass('active');
        return false;
    });
    
    $('.searh-pop .close').click(function(){
        $('.searh-pop').removeClass('active');
        return false;
    });
    
    $('.scroll-to-product-model-bottom').on('click', function(){
        $('#content-wrapper').animate({'scrollTop':$('.product-model-bottom').offset().top-parseInt($('.product-model-bottom').css('padding-top'))});
        return false;
    });
    
    $('.replacement-parts-thumbs img').on('click', function(){
        $('.replacement-parts-thumbs-wrap').slideToggle();
        $('.replacement-parts-thumbs-popup').slideToggle(function(){
            $('.close-parts').addClass('active');    
        });
        return false;
    });
    
    $('.close-parts').on('click', function(){
        $('.close-parts').removeClass('active');  
        $('.replacement-parts-thumbs-wrap').slideToggle();
        $('.replacement-parts-thumbs-popup').slideToggle();
        return false;
    });

    $('.zoom').on('click', function(){
        $('.zoom-popup').addClass('active');
        return false;
    });
    
    $('.zoom-popup .close-popup').on('click', function(){
        $('.zoom-popup').removeClass('active');
        return false;
    });
    
    $('.products-detail-img-thumbs div').on('click', function(){
        $('.products-detail-img-thumbs div').removeClass('active');
        $(this).addClass('active');
        $('.products-detail-img img').attr('src',$(this).find('img').attr('data-big'));
        return false;
    });
    
    $('.play').on('click', function(){
        $('.video-popup').addClass('active');
        $('.video-popup-in iframe').attr('src',$(this).attr('data-play'));   
        return false;
    });
    
    $('.close-video').on('click', function(){
        $('.video-popup').removeClass('active');
        $('.video-popup-in iframe').attr('src','');   
        return false;
    });
    
	//menu click in responsive
    $('.mob-icon').on('click', function(){
        if($(this).hasClass('act')){
            $('.mob-icon').removeClass('act');
            $('header').removeClass('act-mob');
        }else{
            $('.mob-icon').addClass('act');
            $('header').addClass('act-mob');
        }   
    });
    
    var obj;
	$('.play, .open-video-popup').on('click', function(){
        obj = $(this);
        $('.video-popup').addClass('act-act'); 
        $('.video-popup').addClass('act'); 
        setTimeout(function(){
            $('.video-popup iframe').attr('src',obj.attr('data-src'));
            setTimeout(function(){
                $('.video-popup iframe').addClass('act'); 
                $('.video-popup a').addClass('act');    
            },350);    
        },710);
        return false;
    });
    
    $('.video-popup a').on('click', function(){
        $('.video-popup iframe').removeClass('act'); 
        $('.video-popup a').removeClass('act');
        setTimeout(function(){
            $('.video-popup iframe').attr('src','');
            $('.video-popup').removeClass('act'); 
            setTimeout(function(){
                $('.video-popup').removeClass('act-act');    
            },500);
        },500);
        return false;
    });
    
    //holding
    var finish_holding = 0;
    var holding_nav = 0;
    $('.home-baner.holding-baner .content-meny a').click(function(){
        if(finish_holding)return false;	
		finish_holding = 1;
        holding_nav = $('.home-baner.holding-baner .content-meny a').index(this);
        if($(this).hasClass('active')){
            
        }else{
            $('.home-baner.holding-baner .content-meny a').removeClass('active');
            $(this).addClass('active');
            $('.home-baner.holding-baner .points a').eq(holding_nav).click();
        }
        return false;
    });
    
    if($('.holding-baner').length>0){
        if(!_isresponsive){
            $(window).mousewheel(function(event, delta) { 
                if(!finish_holding){
                    if(delta < 0){
                        if($('.home-baner.holding-baner .content-meny a').index($('.home-baner.holding-baner .content-meny a.active'))==$('.home-baner.holding-baner .content-meny a').length-1){
                            $('#content-wrapper').animate({'scrollTop':'+='+$('footer').outerHeight()},200);
                        }else{
                            $('.home-baner.holding-baner .content-meny a').eq(holding_nav+1).click();
                        }
                    }
                    if(delta > 0){
//                        alert($('#content-wrapper').scrollTop()+$('.home-baner.holding-baner').height()+1);
//                        alert($(document).height());
                        if(holding_nav==0){
                            return false;
                        }
//                        if($('#content-wrapper').scrollTop()+$('.home-baner.holding-baner').height()+1<$(document).height()){
                        if($('#content-wrapper').scrollTop()>0){
                            $('#content-wrapper').animate({'scrollTop':'-='+$('footer').outerHeight()+20},200);	
                        }else{
                            $('.home-baner.holding-baner .content-meny a').eq(holding_nav-1).click();
                        }
                    }
                }
                event.preventDefault();
            });
        }
    }
    
    //form
    $('.open-popup-black').on('click', function(){
        $('.popup-black.form-popup-p').addClass('active');
        //$('#content-wrapper').animate({'scrollTop': ($(document).height()*0.5)-($('.popup-black-in').height()*0.5)-30},10);    
        return false;
    });
    
    $('.open-popup-black-find').on('click', function(){
        $('.popup-black.find').addClass('active');
        //$('#content-wrapper').animate({'scrollTop': ($(document).height()*0.5)-($('.popup-black-in').height()*0.5)-30},10);    
        return false;
    });
    
    $('.done-projets').on('click', function(){
        $('.popup-black-projects').addClass('active');
        return false;    
    });
    
    $('.popup-black-projects .popup-close').on('click', function(){
        $('.popup-black-projects').removeClass('active');
        return false;    
    });
    
    $('.close-popup-black').on('click', function(){
        $('.popup-black').removeClass('active');
        return false;
    });
    
    $('.popup-form input[type="text"], .popup-form textarea').on('focus',function(){
        $(this).parent().addClass('active');
        return false;
    });
    
    $('.popup-form input[type="text"], .popup-form textarea').on('blur',function(){
        if($(this).val() == ""){
            $(this).parent().removeClass('active');
        }    
        return false;
    });
    
    $('.popup-form .select').on('click',function(){
        if($(this).hasClass('active')){
            $(this).removeClass('active');    
        }else{
            $(this).addClass('active');
        }
    });

	/*==========================*/
	/* 09 - ajax*/
	/*==========================*/
//    $(document).on('click','.news-thumbs', function(){
//        $('.detail-popup').addClass('active');
//        return false;
//    });
    
//    $(document).on('click','.team-thumbs', function(){
//        $('.detail-popup').addClass('active');
//        return false;
//    });
    
    $(document).on('click','.detail-popup .close', function(){    
        $('body').removeClass('stopscroll');
        $('html').removeClass('stopscroll');
        $('.top-border').removeClass('stopscroll');
        $('.detail-popup').removeClass('active');
        $('.detail-popup .slider-wrap').removeClass('active');
        linck_detail =$(this).attr('href');
        setLocation(linck_detail);
        return false;
    });
    
    //preload///////////////////////////////////////////////////////////////////////////////////////////////
	function showprogress() {
		if (document.images.length === 0) {return false;}
		var loaded = 0;
		for (var i=0; i<document.images.length; i++) {
			if (document.images[i].complete) {
				loaded++;
			}
		}
		percentage  = (loaded / document.images.length);
	}
	var ID, percentage;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //linck/////////////////////////////////////////////////////////////////////////////////////////////////
    function setLocation(curLoc){
	  try {
	   history.pushState(null, null, curLoc);
	   return false;
	  } catch(e) {}
	  location.hash = '#' + curLoc;
	}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//ajax//////////////////////////////////////////////////////////////////////////////////////////////////
	function ajax_linck(){
		$.ajax({
			type:"GET",
			async:false,
			url:linck,
			success:function(response){
				var responseObject = $($.parseHTML(response));
                $('<div class="company-thumbs active"></div>').eq(0).insertAfter('.company-thumbs');
                ID = window.setInterval(function(){
                showprogress();
                    if (percentage == 1) {
                        window.clearInterval(ID);
                        percentage = 0;
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////
                        $('#content-wrapper').scrollTop(0);
                        
                        $('.company-thumbs').eq(1).html(responseObject.find('.company-thumbs').eq(0).html());
//                        $('body').addClass('scroll');
//                        $('html').addClass('scroll');
                        $('.company-thumbs').addClass('pre-animated')
                        $('.company-thumbs').addClass('animated');
                        var scroll_top = $('#content-wrapper').scrollTop();
                        $('.company').css('top','scroll_top');
            //return false;
                        setTimeout(function(){
                            $('.company-thumbs').addClass('animated-next');
                            setTimeout(function(){
                                $('.company-thumbs').eq(0).remove();   
                                $('.company-thumbs').removeClass('animated');
                                setTimeout(function(){
                                    $('.company-thumbs').removeClass('pre-animated');
                                    $('.company-thumbs').removeClass('animated-next');
//                                    $('body').removeClass('scroll');
//                                    $('html').removeClass('scroll');
                                    finish_ajax = 0;
                                    stop_scroll_ajax = 0;
                                    $('body').removeClass('load');
                                    //parallax_stop = 0;
                                },1000);    
                            },1000);
                        },1000);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////
                    }
                });
			}
		});
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	//tranzit-linck/////////////////////////////////////////////////////////////////////////////////////////
    var content_meny_index = 0;
    var linck;
    var finish_ajax = 0;
    var stop_scroll_ajax = 0;
    $(document).on('click','.tranzit-linck', function(){    
        if(!_isresponsive){
            if (finish_ajax) return false;
            finish_ajax = 1;	
            $('body').addClass('load');
            //parallax_stop = 1;
            stop_scroll_ajax = 1;
            content_meny_index = $('.tranzit-linck').index(this);
            linck = $(this).attr('href');
            if(!$(this).hasClass('active')){
                $('.content-meny a').removeClass('active');
                $('.content-meny a').eq(content_meny_index).addClass('active');    
            }
            setLocation(linck);
            ajax_linck();
            return false;
        }
    });
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //tranzit-linck/////////////////////////////////////////////////////////////////////////////////////////
    var content_meny_index_detail = 0;
    var linck_detail;
    var finish_ajax_detail = 0;
    $(document).on('click','.tranzit-linck-detail', function(){  
        if(!_isresponsive){
            if (finish_ajax_detail) return false;
            finish_ajax_detail = 1;	
            $('body').addClass('load');
            //parallax_stop = 1;
            content_meny_index_detail = $('.tranzit-linck').index(this);
            linck_detail = $(this).attr('href');
            setLocation(linck_detail);
            ajax_linck_detail();
            return false;
        }
    });
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    //ajax//////////////////////////////////////////////////////////////////////////////////////////////////
	function ajax_linck_detail(){
		$.ajax({
			type:"GET",
			async:false,
			url:linck_detail,
			success:function(response){
				var responseObject_detail = $($.parseHTML(response));
				$('.detail-popup').html(responseObject_detail.find('.detail-popup').html());
                $('.slider-wrap').removeClass('active');
                ID = window.setInterval(function(){
                showprogress();
                    if (percentage == 1) {
                        window.clearInterval(ID);
                        percentage = 0; ////////////////////////////////////////////////////////////////////////////////////////////////////////
                        initSwiper();
                        setTimeout(function(){
                            setTimeout(function(){
//                                $('body').addClass('stopscroll');
//                                $('html').addClass('stopscroll');
                                $('.top-border').addClass('stopscroll');
                                setTimeout(function(){
                                    $('.detail-popup .slider-wrap').addClass('active');
                                },400);
                            },800);
                            $('.detail-popup').addClass('active');
                            finish_ajax_detail = 0;
                            $('body').removeClass('load');
                            //parallax_stop = 0;
                        },100);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////
                    }
                });
			}
		});
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////

    $('#view-1').on('click', function(){
        $('#view-2').removeClass('active');
        $(this).addClass('active');
        $('.search-row-wrapper-fluid').removeClass('list');
    });

    $('#view-2').on('click', function(){
        $('#view-1').removeClass('active');
        $(this).addClass('active');
        $('.search-row-wrapper-fluid').addClass('list');
    });

    var ajaxFinish = 0;

    $('.ajax-detail').on('click', function(e){
        if(ajaxFinish) return false;

        if(!_ismobile && !_isresponsive){
            e.preventDefault();
            ajaxFinish = 1;
            var url = $(this).attr('href');
            setLocation(url);
            $('body').addClass('detail-ajax');


                $.ajax({
                    type:"GET",
                    async:true,
                    url: '_ajax_'+url,
                    success:function(response){
                        var responseObject = $($.parseHTML(response));
                        $('.ajax-detail-wrapper').html(responseObject);
                        $('.search-detail-arrow.left').attr('href', responseObject.data('prev'));
                        $('.search-detail-arrow.right').attr('href', responseObject.data('next'));

                        ID = window.setInterval(function(){
                            showprogress();
                            if (percentage == 1) {
                                window.clearInterval(ID);
                                percentage = 0;

                                $('body').removeClass('detail-ajax');
                                $('.search-detail-popup').addClass('active');
                                $('.detail-popup-counter span.current').text(responseObject.data('number'));
                                $('html').addClass('overflow-hidden');
                                ajaxFinish = 0;
                            }
                        }, 500);
                    }
                });

        }
    });

    $('.search-detail-popup .button-close').on('click', function(e){
        e.preventDefault();
        $('.search-detail-popup').removeClass('active');
        $('html').removeClass('overflow-hidden');
        setLocation($(this).attr('href'));
        setTimeout(function(){
            $('.search-detail-wrapper').remove();
        }, 500);
    });

    $('.search-detail-arrow.right').on('click', function(e){
        if(ajaxFinish) return false;

        if(!_ismobile && !_isresponsive){
            e.preventDefault();
            ajaxFinish = 1;
            var url = $(this).attr('href');
            setLocation(url);
            $('body').addClass('detail-ajax');

            $.ajax({
                type:"GET",
                async:true,
                url: '_ajax_'+url,
                success:function(response){
                    var responseObject = $($.parseHTML(response)).addClass('right-add');
                    responseObject.insertAfter($('.search-detail-wrapper').addClass('left-remove'));
                    $('.search-detail-arrow.left').attr('href', responseObject.data('prev'));
                    $('.search-detail-arrow.right').attr('href', responseObject.data('next'));

                    ID = window.setInterval(function(){
                        showprogress();
                        if (percentage == 1) {
                            window.clearInterval(ID);
                            percentage = 0;

                            $('body').removeClass('detail-ajax');
                            $('.left-remove').remove();
                            $('.right-add').removeClass('right-add');
                            $('.detail-popup-counter span.current').text(responseObject.data('number'));
                            setTimeout(function(){
                                ajaxFinish = 0;
                            }, 300);
                        }
                    }, 500);
                }
            });
        }
    });

    $('.search-detail-arrow.left').on('click', function(e){
        if(ajaxFinish) return false;

        if(!_ismobile && !_isresponsive){
            e.preventDefault();
            ajaxFinish = 1;
            var url = $(this).attr('href');
            setLocation(url);
            $('body').addClass('detail-ajax');

            $.ajax({
                type:"GET",
                async:true,
                url: '_ajax_'+url,
                success:function(response){
                    var responseObject = $($.parseHTML(response)).addClass('left-add');
                    responseObject.insertBefore($('.search-detail-wrapper').addClass('right-remove'));
                    $('.search-detail-arrow.left').attr('href', responseObject.data('prev'));
                    $('.search-detail-arrow.right').attr('href', responseObject.data('next'));

                    ID = window.setInterval(function(){
                        showprogress();
                        if (percentage == 1) {
                            window.clearInterval(ID);
                            percentage = 0;

                            $('body').removeClass('detail-ajax');
                            $('.right-remove').remove();
                            $('.left-add').removeClass('left-add');
                            $('.detail-popup-counter span.current').text(responseObject.data('number'));
                            setTimeout(function(){
                                ajaxFinish = 0;
                            }, 300);
                        }
                    }, 500);
                }
            });
        }
    });
    



/*=======================================================================================*/

        //menu dropdown-plus
   $('.dropdown-plus span').on('click', function(){
        var overmenu_index = $('.dropdown-plus span').index(this);
        if($('.dropdown-plus').eq(overmenu_index).hasClass('active clicked')){
            $('.dropdown-plus').eq(overmenu_index).removeClass('active clicked');
        } else{
            $('.dropdown-plus').eq(overmenu_index).addClass('active clicked');
        }
    });

/*header scroll*/
 $(window).scroll(function() {

        if ($(this).scrollTop() > 30){  
            $('.header-style').addClass('scrolled');
        }
        else{
            $('.header-style').removeClass('scrolled');
        }
    });


 //overlay hamburger-icon
    $('.hamburger-icon').on('click', function(){
        $('.overlay-wrapper').addClass('active');
    });

//overlay video
$('.button-play').on('click', function(){
        $('.overlay-wrapper.video').addClass('active');
        var $this = $(this);
        if($(this).hasClass('open-video')) setTimeout(function(){$('iframe').attr('src', $this.data('src'));}, 500);
});

//overlay btn-close
    $('.btn-close').on('click', function(){
        $('.overlay-wrapper').removeClass('active');
        var $this = $(this);
        $(this).closest('.overlay-wrapper.video').find('iframe').attr('src', '');
        $('.overlay-wrapper.video').removeClass('active');
    });


//header-menu hamburger-icon2
    $('.hamburger-icon2').on('click', function(){
        if($('.header-style2').hasClass('active')){
            $('.header-style2').removeClass('active');
        }else
            $('.header-style2').addClass('active');
    });
//header-menu hamburger-icon3
    $('.hamburger-icon3').on('click', function(){
        if($('.header-style3').hasClass('active')){
            $('.header-style3').removeClass('active');
        }else
            $('.header-style3').addClass('active');
    });
//left-panel btn
    $('.left-panel-btn').on('click', function(){
        if($('.left-panel').hasClass('active')){
            $('.left-panel').removeClass('active');
        }else
            $('.left-panel').addClass('active');
    });

//content-menu click
    $('.content-menu .button-drop').on('click', function(){
        var list = $('.content-menu ul');
            if($(this).hasClass('active')){
            $(this).removeClass('active');
            list.removeClass('active');
        }else{
            $(this).addClass('active');
            list.addClass('active');
        }
        return false;
    });
    $('.content-menu ul li a').on('click', function(){
        var content_menu_index = $('.content-menu ul li a').index(this);
        var content_menu_btn = $('.content-menu .button-drop a span');
        $('.content-menu ul li a').removeClass('active');
        $('.content-menu ul li a').eq(content_menu_index).addClass('active');
        content_menu_btn.text($(this).data('name'));
        return false;
    });

    /*footer input click*/
    $('.input-style input').on('focus',function(){
        var input_index = $('.input-style input').index(this);
         $('.input-style').eq(input_index).addClass('focus');
         return false;
     });
    $('.input-style input').on('focusout',function(){
        var input_index = $('.input-style input').index(this);
        if($(this).val()==""){
            $('.input-style').eq(input_index).removeClass('focus');
        }
         return false;
    });
    /*footer textarea click*/
    $('.input-style.textarea textarea').on('focus',function(){
        var form_index = $('.input-style.textarea textarea').index(this);
         $('.input-style.textarea').eq(form_index).addClass('focus');
         return false;
     });
    $('.input-style.textarea textarea').on('focusout',function(){
        var form_index = $('.input-style.textarea textarea').index(this);
        if($(this).val()==""){
            $('.input-style.textarea').eq(form_index).removeClass('focus');
        }
         return false;
    });

/*swiper-slide click*/
    $('.swiper-slide').on('click', function(){
        var imgSrc = $(this).data('img-src');
        $('.swipers-img img').attr('src', imgSrc);
    });

    /*Lightbox*/
    if($('.lightbox').length > 0){
        $(function(){
            var lightbox = $('.lightbox').simpleLightbox({
                disableScroll: false,
                navText: ['&#8592;','&#8594;'],
                showCounter: false
            });
        });
    }



});