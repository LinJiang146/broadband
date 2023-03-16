let spans = document.getElementsByTagName("span");
	  let choosedVal = 10; // 评分分数
	  let starWidth = 30; // 星星宽度
	  let _left = document.getElementById('star_div').getBoundingClientRect().left;
	  let _top = document.getElementById('star_div').getBoundingClientRect().top;
	
	  οnlοad = function () {
	    document.getElementById("star_div").onmousemove = function (e) {
	      if(e.target.tagName === 'SPAN') {
	        let index = parseInt(e.target.id);
	        let imgDynamicWidth = e.pageX - _left - index * starWidth;
	        if (imgDynamicWidth < starWidth / 2) {
	          spans[index].className = 'half-star'
	        } else {
	          spans[index].className = 'full-star'
	        }
	        for (let j = index + 1; j < 5; j++) {
	          spans[j].className = 'empty-star'
	        }
	        for (let k = 0; k < index; k++) {
	          spans[k].className = 'full-star'
	        }
	      }
	    };
	
	    document.getElementById("star_div").onclick = function (e) {
	      if(e.target.tagName === 'SPAN') {
	        let index = parseInt(e.target.id);
	        choosedVal = index * 2;
	        let imgDynamicWidth = e.pageX - _left - index * starWidth;
	        if (imgDynamicWidth < starWidth / 2) {
	          choosedVal += 1
	        } else {
	          choosedVal += 2
	        }
	      }
	    };
	
	    //载入鼠标离开div事件
	    document.getElementById("star_div").onmouseleave = function () {
	      let index = parseInt(choosedVal / 2);
	
	      for (let j = index; j < 5; j++) {
	        spans[j].className = 'empty-star'
	      }
	      for (let k = 0; k < index; k++) {
	        spans[k].className = 'full-star'
	      }
	      if(index * 2 !== choosedVal) {
	        spans[index].className = 'half-star'
	      }
	    };
	  }();