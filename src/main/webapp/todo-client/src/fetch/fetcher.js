import BuildingContainer from "../container/building-container";
import PersonContainer from "../container/person-container";

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

    static _getLink(rel) {
        return this._TODO_API_BASE_URL + this._parseLink(rel);
    }

    static _parseLink(rel) {
        return this._LINKS
            .find(link => link.rel === rel).href
    }

    static _post(link, ob) {
        const stringifyedOb = JSON.stringify(ob);
        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: stringifyedOb
        };
        return fetch(link, requestOptions)
            .catch(err => console.error(`Posting ${stringifyedOb} failed`, err));
    }

    static _get(link) {
        return fetch(link)
            .then(r => r.json());
    }
}