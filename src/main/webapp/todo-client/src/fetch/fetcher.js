import BuildingContainer from "../container/building-container";
import PersonContainer from "../container/person-container";
import TaskContainer from "../container/task-container";

export default class Fetcher {

    static _PERSON = "person";
    static _BUILDING = "building";
    static _TASK = "task";

    static _SERVICE_URL = 'http://localhost:8080';
    static _TODO_API_BASE_URL = `${this._SERVICE_URL}/api/v1/todo/`;
    static _LINKS = [
        { rel: this._PERSON, href: "persons" },
        { rel: this._BUILDING, href: "buildings" },
        { rel: this._TASK, href: "tasks" }
    ];

    static createPerson(personContainer) {
        const link = this._getLink(this._PERSON);
        return this._post(link, personContainer);
    }

    static createBuilding(buildingContainer) {
        const link = this._getLink(this._BUILDING);
        return this._post(link, buildingContainer);
    }

    static createTask(taskContainer) {
        const link = this._getLink(this._TASK);
        return this._post(link, taskContainer);
    }

    static getAllPersons() {
        const link = this._getLink(this._PERSON);
        return this._get(link)
        .then(persons => persons.map(p => new PersonContainer(p)));
    }

    static getAllBuildings() {
        const link = this._getLink(this._BUILDING);
        return this._get(link)
            .then(buildings => buildings.map(p => new BuildingContainer(p)));
    }

    static getTasks(assigneeId, buildingId) {
        let link = this._getLink(this._TASK);

        let numberOfQueryParams = assigneeId ? 1 : 0;
        numberOfQueryParams += buildingId ? 1 : 0;
        
        if (numberOfQueryParams > 0) {
            link += '?';
        }

        if (assigneeId) {
            link += 'assignee_id=' + assigneeId;
        }

        if (numberOfQueryParams > 1) {
            link += '&';
        }

        if (buildingId) {
            link += 'building_id='+ buildingId;
        }

        return this._get(link)
        .then(tasks => tasks.map(t => new TaskContainer(t)));
    }

    static putTask(taskContainer) {
        let link = this._getLink(this._TASK) + '/' + taskContainer.getId();
        this._put(link, taskContainer.toJSON())
    }

    static _getLink(rel) {
        return this._TODO_API_BASE_URL + this._parseLink(rel);
    }

    static _parseLink(rel) {
        return this._LINKS
            .find(link => link.rel === rel).href
    }

    static _post(link, ob) {
        return this._write(link, ob, "POST");
    }

    static _put(link, ob) {
        return this._write(link, ob, "PUT");
    }

    static _write(link, ob, method) {
        const stringifyedOb = JSON.stringify(ob);
        const requestOptions = {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: stringifyedOb
        };
        return fetch(link, requestOptions)
            .catch(err => {
                console.error(`Posting ${stringifyedOb} failed`, err);
                throw err;
            });
    }

    static _get(link) {
        return fetch(link)
            .then(r => r.json());
    }
}
