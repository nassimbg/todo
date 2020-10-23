export default class TaskContainer {
    name;
    assigneeId;
    buildingId;
    status;

    static _statusMapper = [
        {
            code : 0,
            description: "Not Started",
            queryValue: "NOT_STARTED"
        },
        {
            code : 1,
            description: "Done",
            queryValue: "DONE"
        }
    ]

    constructor(t) {
        if (t) {
            this.name = t.name;
            this.id = t.id;
            this.assigneeId = t.assigneeId;
            this.buildingId = t.buildingId;
            this.status = TaskContainer._statusMapper.find(sm => sm.queryValue === t.status);
        } else {
            this.status = TaskContainer._statusMapper.find(sm => sm.queryValue === "NOT_STARTED");
        }
    }

    getName() {
        return this.name;
    }

    getId() {
        return this.id;
    }

    getAssigneeId() {
        return this.assigneeId;
    }

    getBuildingId() {
        return this.buildingId;
    }

    getStatusDescription() {
        return this.status.description;
    }

    getStatusCode() {
        return this.status.code;
    }

    setStatus(statusCode) {
        this.status = TaskContainer.getStatues().find(s => s.code === statusCode);
        console.log(this.status)
    }

    static getStatues() {
        return TaskContainer._statusMapper
    }

    toJSON() {
        return {
            name: this.name,
            id : this.id,
            assigneeId : this.assigneeId,
            buildingId : this.buildingId,
            status : this.status.queryValue
        }
    }

}
