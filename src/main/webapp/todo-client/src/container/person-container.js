export default class PersonContainer {
    name;
    id;

    constructor(p) {
        if (p) {
            this.name = p.name;
            this.id = p.id;
        }
    }

    getName() {
        return this.name;
    }

    getId() {
        return this.id;
    }
}