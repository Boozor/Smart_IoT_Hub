/**
 * A model for managing members in groups.
 */
function create_members_model(groups) {
	// create the data structure

	var all_members = new Set(); // all unique member names
	var group_names = [];
	var group_members = new Map(); // group_name to set of group members 
	var member_state= new Map();
	for (var group of groups) {
		group_names.push(group.name);
		var nameList=new Array();
		for (var i=0;i<group.members.length;i++){
			nameList.push(group.members[i].name);
			member_state.set(group.members[i].name,group.members[i].state);
		}
		var members = new Set(nameList);
		group_members.set(group.name, members);
		members.forEach(member => all_members.add(member));
	}
	var member_names = Array.from(all_members);
	group_names.sort();
	member_names.sort();

	// create the object
	var that = {}
	that.get_group_names = () => group_names;
	that.get_member_names = () => member_names;
	that.is_member_in_group = (member_name, group_name) =>
		!group_members.has(group_name)? false:
			group_members.get(group_name).has(member_name);
	that.get_group_members = group_name => group_members.get(group_name);
	that.get_members_state = member_names => member_state.get(member_names);
	console.debug("Members Model",
		groups, group_names, member_names, group_members,member_state);

	return that;
}

/**
 * The Members controller holds the state of groups.
 * It creates its view in render().
 */
class Members extends React.Component {

	constructor(props) {
		super(props);
		console.info("Members constructor()");
		this.state = {
			members: create_members_model([
			]),
			inputName:"",
			inputMembers:"",
		};
	}

	componentDidMount() {
		console.info("Members componentDidMount()");
		this.getGroups();
		setInterval(this.getGroups, 1000);
	}

	render() {
		console.info("Members render()");
		return (<MembersTable members={this.state.members} 
			inputName={this.state.inputName} 
			inputMembers={this.state.inputMembers}
			onInputNameChange={this.onInputNameChange}
			onInputMembersChange={this.onInputMembersChange}
			onMemberChange={this.onMemberChange}
			onAddGroup={this.onAddGroup}
			onDeleteGroup={this.onDeleteGroup} 
			onAddMemberToAllGroups={this.onAddMemberToAllGroups}
			onChangeGroupToOn={this.onChangeGroupToOn}
			onChangeGroupToOff={this.onChangeGroupToOff}
			onChangeGroupToggle={this.onChangeGroupToggle}
			oncreateGroup={this.oncreateGroup}
			onDeleteMemberToAllGroups={this.onDeleteMemberToAllGroups}
			/>);
	}
	getGroups = () => {
		console.info("RESTful: get groups");
		fetch("api/groups")
			.then(rsp => rsp.json())
			.then(groups => this.showGroups(groups))
			.catch(err => console.error("Members: getGroups", err));
	}

	showGroups = groups => {
		this.setState({
			members: create_members_model(groups)
		});
	}
	oncreateGroup = (groupName, groupMembers) => {
		this.createGroup(groupName,groupMembers);
	}
	createGroup = (groupName, groupMembers) => {
		console.info("RESTful: create group "+groupName
			+" "+JSON.stringify(groupMembers));
		
		var postReq = {
			method: "POST",
			headers: {"Content-Type": "application/json"},
			body: JSON.stringify(groupMembers)
		};
		fetch("api/groups/"+groupName, postReq)
			.then(rsp => this.getGroups())
			.catch(err => console.error("Members: createGroup", err));
	}
	createManyGroups = groups => {
		console.info("RESTful: create many groups "+JSON.stringify(groups));
		var pendingReqs = groups.map(group => {
			var postReq = {
				method: "POST",
				headers: {"Content-Type": "application/json"},
				body: JSON.stringify(group.members)
			};
			return fetch("api/groups/"+group.name, postReq);
		});

		Promise.all(pendingReqs)
			.then(() => this.getGroups())
			.catch(err => console.error("Members: createManyGroup", err));
	}
	deleteGroup = groupName => {
		console.info("RESTful: delete group "+groupName);
	
		var delReq = {
			method: "DELETE"
		};
		fetch("api/groups/"+groupName, delReq)
			.then(rsp => this.getGroups())
			.catch(err => console.error("Members: deleteGroup", err));
	}	
	onInputNameChange = value => {
		console.debug("Members: onInputNameChange", value);
		this.setState({inputName: value});
	}

	onInputMembersChange = value => {
		console.debug("Members: onInputMembersChange", value);
		this.setState({inputMembers: value});
	}
	onMemberChange = (memberName, groupName) => {
		var groupMembers = new Set(this.state.members.get_group_members(groupName));
		if (groupMembers.has(memberName))
			groupMembers.delete(memberName);
		else
			groupMembers.add(memberName);

		this.createGroup(groupName, Array.from(groupMembers));
	}
	onAddGroup = () => {
		var name = this.state.inputName;
		var members = this.state.inputMembers.split(',');
		
		this.createGroup(name, members);
	}
	onDeleteGroup = groupName => {
		this.deleteGroup(groupName);
	}
	onChangeGroupToOn = groupName => {
		this.ChangeGroupToOn(groupName);
	}
	onChangeGroupToOff = groupName => {
		this.ChangeGroupToOff(groupName);
	}
	onChangeGroupToggle = groupName => {
		this.ChangeGroupToggle(groupName);
	}
	ChangeGroupToOn = groupName => {
		var url = "../api/groups/" + groupName + "?action=on";
		console.info("ChangeGroupToOn: request " + url);
		fetch(url);
	}
	onChangeGroupToOff = groupName => {
		this.ChangeGroupToOff(groupName);
	}
	ChangeGroupToOff = groupName => {
		var url = "../api/groups/" + groupName + "?action=off";
		console.info("ChangeGroupToOn: request " + url);
		fetch(url);
	}
	onChangeGroupToggle = groupName => {
		this.ChangeGroupToggle(groupName);
	}
	ChangeGroupToggle = groupName => {
		var url = "../api/groups/" + groupName + "?action=toggle";
		console.info("ChangeGroupToOn: request " + url);
		fetch(url);
	}
	onAddMemberToAllGroups = memberName => {
		var groups = [];
		for (var groupName of this.state.members.get_group_names()) {
			var groupMembers = new Set(this.state.members.get_group_members(groupName));
			groupMembers.add(memberName);
			groups.push({name: groupName, members: Array.from(groupMembers)});
		}
		this.createManyGroups(groups);
	}
	onDeleteMemberToAllGroups = memberName => {
		var groups=[];
		for (var groupName of this.state.members.get_group_names()) {
			var groupMembers = new Set(this.state.members.get_group_members(groupName));
			groupMembers.delete(memberName);
			groups.push({name: groupName, members: Array.from(groupMembers)});
		}
		this.createManyGroups(groups);
	}
}

// export
window.Members = Members;

