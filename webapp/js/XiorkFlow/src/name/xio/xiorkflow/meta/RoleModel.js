
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p></p>
 * @author  wang 
 */
 
function RoleModel() {

}
RoleModel.prototype = new RoleModel();

//
RoleModel.prototype.setName = function (name) {
	this.name=name;
};
RoleModel.prototype.getName = function () {
	return this.name;
};

RoleModel.prototype.setId = function (id) {
	this.id=id;
};
RoleModel.prototype.getId = function () {
	return this.id;
};