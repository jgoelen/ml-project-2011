package sc

class Task {
	 
    String description
    Integer order = 0
    Boolean isDone = false
 
    static constraints = {
    }
 
    static mapping = {
        order column: "task_order"
    }
      
}
