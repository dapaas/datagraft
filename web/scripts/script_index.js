(function() {

  $(function() {

    /*  Globals
    -------------------------------------------------- */
    var PROPERTIES =               ['translateX', 'translateY', 'opacity', 'rotate', 'scale'],
        $window =                  $(window),
        $body =                    $('body'),
        wrappers =                 [],
        currentWrapper =           null,
        scrollTimeoutID =          0,
        bodyHeight =               0,
        windowHeight =             0,
        windowWidth =              0,
        prevKeyframesDurations =   0,
        scrollTop =                0,
        relativeScrollTop =        0,
        currentKeyframe =          0,
        keyframes = [
          {
            'wrapper' : '#intro',
            'duration' : '100%',
            'animations' :  [
              {
                'selector'    : 'H1',
                'translateY'  : -140,
                'opacity'     : 0
              } , {
                'selector'    : 'H2',
                'translateY'  : -110,
                'opacity'     : 0
              } , {
                'selector'    : '.twitter',
                'opacity'     : [1, 0]
              }
            ]
          } , {
            'wrapper' : '#images',
            'duration' : '150%',
            'animations' :  [
              {
                'selector'    : '.images-byline',
                'translateY'  : '-25%',
                'opacity'     : [0, 1.75] // hack to accelrate opacity speed
              } , {
                'selector'    : '#homepage',
                'translateY'  : '-90%'
              } , {
                'selector'    : '.iphone',
                'translateY'  : '-66%'
              }
            ]
          } , {
            'wrapper' : '#images',
            'duration' : '75%',
            'animations' :  [
              {
                'selector'    : '#screenshot1',
                'translateX'  : '0%',
              } ,
              {
                'selector'    : '.iphone',
                'translateY'  : ['-66%', '-66%'], 
                'translateX'  : ['0%', '0%']
              }
            ]
          } , {
            'wrapper' : '#images',
            'duration' : '150%',
            'animations' :  [
              {
                'selector'    : '.images-byline',
                'translateY'  : ['-25%', '-25%'],
                'scale'       : .7,
                'opacity'     : [1.75, -.75] // hack to accelrate opacity speed
              } , {
                'selector'    : '.images-byline-2',
                'opacity'     : [0, 1],
                'translateY'  : '-15%'
              } , {
                'selector'    : '#homepage',
                'translateY'  : ['-90%', '-90%'],
                'scale'       : .8,
                'opacity'     : -.75
              } , {
                'selector'    : '.iphone',
                'translateY'  : ['-66%', '-90%'],
                'translateX'  : '-0%',
                'rotate'      : -90,
                'scale'       : 1.3
              } , {
                'selector'    : '#screenshot1',
                'scale'       : .9,
                'translateX'  : '20%',
              } , {
                'selector'    : '#screenshot2',
                'scale'       : [.5, 1]
              }
            ]
          } , {
            'wrapper' : '#images',
            'duration' : '40%',
            'animations' :  []
          } , {
            'wrapper' : '#images',
            'duration' : '150%',
            'animations' :  [
              {
                'selector'    : '.images-byline-2',
                'opacity'     : [1, .5],
                'translateY'  : ['-15%', '50%'],
                'opacity'     : [1, -2]
              } , {
                'selector'    : '.iphone',
                'translateY'  : ['-90%', '5%'],
                'translateX'  : ['-0%', '-0%'],
                'rotate'      : [-90, -90],
                'scale'       : [1.3, 1.3]
              } , {
                'selector'    : '#screenshot1',
                'translateX'  : ['20%', '20%']
              } , {
                'selector'    : '#screenshot2',
                'scale'       : [1, 1]
              }
            ]
          } , {
            'wrapper' : '#links',
            'duration' : '100%',
            'animations' :  [
              {
                'selector'    : '#links',
                'opacity'     : [0, 2],
                'scale'       : [.8, 1]
              } , {
                'selector'    : '.twitter',
                'opacity'     : [0, 1]
              }
            ]
          } , {
            'duration' : '100%',
            'animations' :  []
          }
        ],

    /*  Construction
    -------------------------------------------------- */
    init = function() {
      scrollIntervalID = setInterval(updatePage, 10);
      setupValues();
      $window.resize(throwError);
      if(isTouchDevice) {
        $window.resize(throwError);
      }
      $("#indexscrollf").click(function(){
        prevKeyframesDurations += keyframes[currentKeyframe].duration;
        currentKeyframe++;
        showCurrentWrappers();
        $("html, body").animate({scrollTop: keyframes[currentKeyframe].duration*2}, 300);
      });
    },

    setupValues = function() {
      scrollTop = $window.scrollTop();
      windowHeight = $window.height();
      windowWidth = $window.width();
      convertAllPropsToPx();
      buildPage();
    },

    buildPage = function() {
      var i, j, k;
      for(i=0;i<keyframes.length;i++) { // loop keyframes
          bodyHeight += keyframes[i].duration;
          if($.inArray(keyframes[i].wrapper, wrappers) == -1) {
            wrappers.push(keyframes[i].wrapper);
          }
          for(j=0;j<keyframes[i].animations.length;j++) { // loop animations
            Object.keys(keyframes[i].animations[j]).forEach(function(key) { // loop properties
              value = keyframes[i].animations[j][key];
              if(key !== 'selector' && value instanceof Array === false) {
                var valueSet = [];
                valueSet.push(getDefaultPropertyValue(key), value);
                value = valueSet;
              }
              keyframes[i].animations[j][key] = value;
            });
          }
      }
      $body.height(bodyHeight);
      $window.scroll(0);
      currentWrapper = wrappers[0];
      $(currentWrapper).show();
    },

    convertAllPropsToPx = function() {
      var i, j, k;
      for(i=0;i<keyframes.length;i++) { // loop keyframes
        keyframes[i].duration = convertPercentToPx(keyframes[i].duration, 'y');
        for(j=0;j<keyframes[i].animations.length;j++) { // loop animations
          Object.keys(keyframes[i].animations[j]).forEach(function(key) { // loop properties
            value = keyframes[i].animations[j][key];
            if(key !== 'selector') {
              if(value instanceof Array) { // if its an array
                for(k=0;k<value.length;k++) { // if value in array is %
                  if(typeof value[k] === "string") {
                    if(key === 'translateY') {
                      value[k] = convertPercentToPx(value[k], 'y');
                    } else {
                      value[k] = convertPercentToPx(value[k], 'x');
                    }
                  }
                } 
              } else {
                if(typeof value === "string") { // if single value is a %
                  if(key === 'translateY') {
                    value = convertPercentToPx(value, 'y');
                  } else {
                    value = convertPercentToPx(value, 'x');
                  }
                }
              }
              keyframes[i].animations[j][key] = value;
            }
          });
        }
      }
    },

    getDefaultPropertyValue = function(property) {
      switch (property) {
        case 'translateX':
          return 0;
        case 'translateY':
          return 0;
        case 'scale':
          return 1;
        case 'rotate':
          return 0;
        case 'opacity':
          return 1;
        default:
          return null;
      }
    },

    /*  Animation/Scrolling
    -------------------------------------------------- */
    updatePage = function() {
      window.requestAnimationFrame(function() {
        setScrollTops();
        if(scrollTop > 0 && scrollTop <= (bodyHeight - windowHeight)) {
          animateElements();
          setKeyframe();
        }
      });
    },

    setScrollTops = function() {
      scrollTop = $window.scrollTop();
      relativeScrollTop = scrollTop - prevKeyframesDurations;
    },

    animateElements = function() {
      var animation, translateY, translateX, scale, rotate, opacity;
      for(var i=0;i<keyframes[currentKeyframe].animations.length;i++) {
        animation   = keyframes[currentKeyframe].animations[i];
        translateY  = calcPropValue(animation, 'translateY');
        translateX  = calcPropValue(animation, 'translateX');
        scale       = calcPropValue(animation, 'scale');
        rotate      = calcPropValue(animation, 'rotate');
        opacity     = calcPropValue(animation, 'opacity');

        $(animation.selector).css({
          'transform':    'translate3d(' + translateX +'px, ' + translateY + 'px, 0) scale('+ scale +') rotate('+ rotate +'deg)',
          'opacity' : opacity
        });
      }
    },

    calcPropValue = function(animation, property) {
      var value = animation[property];
      if(value) {
        value = easeInOutQuad(relativeScrollTop, value[0], (value[1]-value[0]), keyframes[currentKeyframe].duration);
      } else {
        value = getDefaultPropertyValue(property);
      }
      // value = +value.toFixed(2) 
      // TEMPORARILY REMOVED CAUSE SCALE DOESN'T WORK WITHA AGRESSIVE ROUNDING LIKE THIS
      return value;
    },

    easeInOutQuad = function (t, b, c, d) {
      //sinusoadial in and out
      return -c/2 * (Math.cos(Math.PI*t/d) - 1) + b;
    };

    setKeyframe = function() {
      if(scrollTop > (keyframes[currentKeyframe].duration + prevKeyframesDurations)) {
          prevKeyframesDurations += keyframes[currentKeyframe].duration;
          currentKeyframe++;
          showCurrentWrappers();
      } else if(scrollTop < prevKeyframesDurations) {
          currentKeyframe--;
          prevKeyframesDurations -= keyframes[currentKeyframe].duration;
          showCurrentWrappers();
      }
    },

    showCurrentWrappers = function() {
      if(keyframes[currentKeyframe].wrapper != currentWrapper) {
        $(currentWrapper).hide();
        if (currentKeyframe == 0){
          //$("#intro-bg-video-container").css("display", "block");
          //document.querySelector('#intro-bg-video').play();
        }else{
          //$("#intro-bg-video-container").css("display", "none");
          //document.querySelector('#intro-bg-video').pause();
        }
        $(keyframes[currentKeyframe].wrapper).show();
        currentWrapper = keyframes[currentKeyframe].wrapper;
      }
    },

    /*  Helpers
    -------------------------------------------------- */

    convertPercentToPx = function(value, axis) {
      if(typeof value === "string" && value.match(/%/g)) {
        if(axis === 'y') value = (parseFloat(value) / 100) * windowHeight;
        if(axis === 'x') value = (parseFloat(value) / 100) * windowWidth;
      }
      return value;
    },

    throwError = function() {
      //$body.addClass('page-error')
    },

    isTouchDevice = function() {
      return 'ontouchstart' in window // works on most browsers 
      || 'onmsgesturechange' in window; // works on ie10
    },

    init();

  });
  

}).call(this);