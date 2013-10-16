
Array.prototype.insert = function (index, item) {
  this.splice(index, 0, item);
};

function skipWord() {
	nextWord();
}

function removeTaggedWord(ev){
	btn=$(ev.target);
	var start=btn.data('start');
	$.post('/dokuments/remove', 
			{"word.id" : btn.data('tagId')},
			function(data) {
				$("#tagInfo").hide();
				var word=$("#w"+start);
				word.data("tag", null);
				word.css("color", '');	
				word.attr('title','');
				var sidx=state.tags.indexOf(start);
				state.tags.splice(sidx, 1);
				state.words.push(start);
				state.words.sort();
			});		
}

state={
		words: [], 
		tagged: []
};

function selectWord(start) {
	var currentWord= $("#w"+start);
	if(state.current>=0) {
		var previousWord=$("#w"+state.current);
		previousWord.removeClass("tagging");
	}
	state.current=start;
	currentWord.addClass('tagging');
	if(currentWord.data('tag')) {
		$("#tagInfo").show();
		tag=currentWord.data('tag');
		$("#tagName").html(tag.tag.name);
		if(tag.user) {
			$("#tagCreator").html(tag.user.username);
		}
		$("#tagRemove").data('tagId',tag.id);
		$("#tagRemove").data('start',tag.start);
	} else {
		$("#tagInfo").hide();
	}
	
	getSuggestion(currentWord.data('word'));
	return currentWord;
}

function scrollToCurrentWord(){
	if(state.current>=0) {
		var currentWord= $("#w"+state.current);
		$("#source").animate({ 
			scrollTop: currentWord.offset().top-300 }, 
			1000);		
	}

}



function nextWord() {
	if(state.current===null) {
		initSelection();
	} else {
		var idx=state.words.indexOf(state.current);
		if(idx<0) {
			initSelection();
		} else if(idx+1<state.words.length){
			selectWord(state.words[idx+1]);
					
		}
	}
}

function initSelection(){
	if(state.words.length>0) {
		selectWord(state.words[0]);		
		scrollToCurrentWord();
	} else {
		alert('Честитки, сите зборови се означени :)');
	}
}

function getAnotate(tagId, tagColor, tagName) {
	var annotate = function(){	
		if(state.current>=0) {
			var currentWord=$("#w"+state.current);
			var start=currentWord.data('start');
			var end=start+currentWord.data('word').length;
			
			$.post('/dokuments/add', {
				taggedWord : {
					start : start,
					end : end,
					"dokument.id" : dokumentId,
					"tag.id" : tagId
				}							
			}, function(data) {
				
				applyTag(data);
			});
			
			nextWord();
			
		}
	};
	return annotate;
}



function wordClick(ev){
	currentWord= $(ev.target);
	var start=currentWord.data('start');
	selectWord(start);
	
}

function getSuggestion(word) {
	$("#selectedWord").html(word);
	$.get('/lexiconcontroller/find?word='+word, 
			function(data) {
		var target=$("#preferedTags");
		target.html("")
		for(var i=0;i<data.length;i++) {
			target.append('<div class="suggested-tag">{0} - {1}</div>'.format(data[i].word, data[i].tag));
		}
	});	
}


function applyTag(tag) {
	var start=tag.start;
	if(tag.tag) {							
		var word=$("#w"+start);
		word.data("tag", tag);
		word.css("color", tag.tag.color);	
		word.attr('title',tag.tag.name);
		var sidx=state.words.indexOf(start);
		state.words.splice(sidx, 1);
		state.tags.push(start);
	}
}

//first, checks if it isn't implemented yet
if (!String.prototype.format) {
	String.prototype.format = function() {
		var args = arguments;
		return this.replace(/{(\d+)}/g, function(match, number) { 
			return typeof args[number] != 'undefined'
				? args[number]
			: match
			;
		});
	};
}


$(document).ready(function(){
	
	$("#tagRemove").click(removeTaggedWord);
	
	$("#scrollToWord").click(scrollToCurrentWord);
	
	
	
	
	
	$.getJSON('/dokuments/getDocument', {'documentId' : dokumentId}, function(data) {
		var patt=/([éабвгдѓежзѕијклљмнњопрстќуфхцчџшАБВГДЃЕЖЗЅИЈКЛЉМНЊОПРСТЌУФХЦЧЏШ1234567890'`]+)/gm;
		txt=data.text;
		txt=txt.replace(/\r/g,'');
		var match=patt.exec(txt);
		var lastEnd=0;
		var targetHtml="";
		var tmpl="{0}<span class='word' id='w{1}' data-start='{1}' data-word='{2}' data-next='{3}'>{2}</span>";
		while (match) {
			var pref='';
			if(lastEnd>0) {
				pref=txt.substring(lastEnd, match.index);
				pref=pref.replace(/\n/g,'<br/>');
			}
			var word=match.pop();
			var idx=match.index;
			lastEnd=match.index+word.length;
			match=patt.exec(txt);
			var next= match && match.index;			
			targetHtml+=tmpl.format(pref,idx,word,next);
			state.words.push(idx)
		}
		$("#source").html(targetHtml);
		$(".word").click(wordClick);
		$("#selectedWord").html("Почекајте додека да се вчитаат таговите...")
		$.post('/dokuments/taggedWords', 
				{"dokument.id" : dokumentId},
				function(data) {
					if(!data) {
						data=[];
					}
					var target=$("#result");
					state.tags=data;
					for(var i=0;i<data.length;i++) {
						var tag=data[i];
						applyTag(tag);
					}
					
					initSelection();
				});
	});
})	

       
