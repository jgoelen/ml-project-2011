Todos = SC.Application.create();

// SUGGESTION: Your tags can be stored in a similar way as how todos are stored
Todos.Todo = SC.Object.extend({
  title: null,
  isDone: false,
  labels: []
});

Todos.Label = SC.Object.extend({
  title: null
});

Todos.labelsController = SC.ArrayProxy.create({
	content: [],
	recommendLabelsFor: function(todo){
		var label = Todos.Label.create({ title: "label 1" });
		this.pushObject(label);
	},
	clearLabels: function(){
		this.set('content', []);
	}
});

Todos.todosController = SC.ArrayProxy.create({
  content: [],

  createTodo: function(title,labels) {
    var todo = Todos.Todo.create({ title: title, labels: labels });
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