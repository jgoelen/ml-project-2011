Todos = SC.Application.create({
	ready: function(){
    	this._super();
    	Todos.todosController.fetchTodos();
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
    
    $.ajax( url, {
      type: method,
      data: JSON.stringify({ description: self.get('title'), isDone: self.get('isDone')}),
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
  title: null
});

Todos.labelsController = SC.ArrayProxy.create({
	content: [],
	recommendLabelsFor: function(todo){
		this.clearLabels();
		$.ajax({ url:'label/recommend', data:{text:todo}, 
      		success: function(data){
        		Todos.labelsController.beginPropertyChanges();
        		data.content.forEach(function(item){
          			var label = Todos.Label.create({ title: item.title });
          			Todos.labelsController.pushObject(label);
        		});
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
          var todo = Todos.Todo.create({
            id: item.id,
            title: item.description,
            isDone: item.isDone
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
    this.filterProperty('isDone', true).forEach(this.removeObject, this);
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
      var labels = Todos.labelsController.get('content');
      Todos.todosController.createTodo(title,labels);
      Todos.labelsController.clearLabels();
      this.set('value', '');
    }
  },
 
  keyDown: function(evt) {
  	var value = this.get('value');
    if (evt.keyCode === Todos.SPACE_KEY) {
    	Todos.labelsController.recommendLabelsFor(value);
    }
  }
  
});

Todos.SPACE_KEY = 32;