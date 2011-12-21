Todos = SC.Application.create({
	ready: function(){
    	this._super();
    	Todos.todosController.fetchTodos();
    	Todos.labelsController.initLabels();
  	}
});

// SUGGESTION: Your tags can be stored in a similar way as how todos are stored
Todos.Todo = SC.Object.extend({
  id: null,
  title: null,
  isDone: false,
  labels: [],
  
  attributes: function(){
    return {
      title: this.get('title'),
      done: this.get('isDone')
    };
  }.property('title', 'isDone').cacheable(),
  
  isNew: function(){
    return !this.get('id');
  }.property('id').cacheable(),
  
  save: function(){
    
    var self = this;
    var url = this.get('isNew') ? 'tasks/' : 'tasks/' + this.get('id');
    var method = this.get('isNew') ? 'POST' : 'PUT';
    
    var labels = self.get('labels').map( function(l){  
    	return {id:l.get('id')}; 
    }, self );
      
    $.ajax( url, {
      type: method,
      data: JSON.stringify({ description: self.get('title'), isDone: self.get('isDone'), labels:labels}),
      mimeType: 'application/json', 
      contentType: 'application/json',
      dataType: 'json', 
      success: function(data) {
        self.set('id', data.content.id);
      }
    });
  },
  
  autosave: function(){
    this.save();
  }.observes('attributes')
  
});

Todos.Label = SC.Object.extend({
  id: null,
  key: null,
  title: null,
  isSelected: false,
  isRecommended: false,
  confidence: null
});

Todos.labelsController = SC.ArrayProxy.create({
	content: [],
	initLabels: function(){ this.recommendLabelsFor(""); },
	resetLabels: function(){ 
		Todos.labelsController.beginPropertyChanges();
		this.setEach('isRecommended', false); 
		this.setEach('isSelected', false);
		Todos.labelsController.endPropertyChanges();
	},
	recommendLabelsFor: function(todo){
		//this.clearLabels();
		var self = this;
		$.ajax({ url:'label/recommendAll', data:{text:todo}, 
      		success: function(data){
      			var content = self.get('content')
      			Todos.labelsController.beginPropertyChanges();
      			if( content.length === 0 ){      				      			
      				var labels = data.content.map( function(it){  
      			    	return Todos.Label.create({id:it.id,key:it.key,title:it.title,isSelected:it.recommend,isRecommended:it.recommend, confidence:it.confidence });
      			    }, self );      				      				
	      			self.set('content', labels);
      			} else {
      				var recommended = data.content.filterProperty('recommend', true);
      				content.forEach( function( label ){
      					var isRecommended = recommended.someProperty('key',label.key);
      					label.set('isRecommended',isRecommended);
      					label.set('isSelected',isRecommended);
      					
      				});      				      				
      			}
      			Todos.labelsController.endPropertyChanges();
      		}
    	});
	},
	clearLabels: function(){
		this.set('content', []);
	}
});

Todos.todosController = SC.ArrayProxy.create({
  content: [],
  fetchTodos: function(){
    $.ajax('tasks', {
      success: function(data){
        Todos.todosController.beginPropertyChanges();
        data.content.forEach(function(item){
          var labels = [];
          item.labels.forEach(function(l){
          	var label = Todos.Label.create({ id: l.id, key: l.key, title: l.title });
          	labels.push(label);
          });
          var todo = Todos.Todo.create({
            id: item.id,
            title: item.description,
            isDone: item.isDone,
            labels:labels
          });
          Todos.todosController.pushObject(todo);
        });
        Todos.todosController.endPropertyChanges();
      }
    });
  },
  
  createTodo: function(title,labels) {
    var todo = Todos.Todo.create({ title: title, labels: labels });
    todo.save();
    this.pushObject(todo);
  },

  clearCompletedTodos: function() {
    var self = this;
    this.filterProperty('isDone', true).forEach(function(it) {
    	$.ajax('tasks/'+it.get('id'), { type: 'DELETE',
      			data: { _method: 'delete' },
      			success: function(){
       				self.removeObject(it);
      			}
    	});
	},this);
  },

  remaining: function() {
    return this.filterProperty('isDone', false).get('length');
  }.property('@each.isDone'),

  allAreDone: function(key, value) {
    if (value !== undefined) {
      this.setEach('isDone', value);
      return value;
    } else {
      return !!this.get('length') && this.everyProperty('isDone', true);
    }
  }.property('@each.isDone')
});

Todos.StatsView = SC.View.extend({
  remainingBinding: 'Todos.todosController.remaining',

  remainingString: function() {
    var remaining = this.get('remaining');
    return remaining + (remaining === 1 ? " item" : " items");
  }.property('remaining')
});


Todos.CreateTodoView = SC.TextField.extend({
  
  insertNewline: function() {
    var title = this.get('value');
    if (title) {
      var labels = Todos.labelsController.get('content').filterProperty('isSelected', true);
      Todos.todosController.createTodo(title,labels);
      Todos.labelsController.resetLabels();
      this.set('value', '');
    }
  },
 
  keyUp: function() {
	this.interpretKeyEvents(event);  
  	var value = this.get('value');
    Todos.labelsController.recommendLabelsFor(value);
    return false;
  }
  
});

Todos.SPACE_KEY = 32;