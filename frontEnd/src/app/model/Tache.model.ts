import {User} from "./User.model";
import {Project} from "./Project.model";

export interface Task{
    id?:number;
    name:string;
    contenu:string;
    user:User;
    project:Project;
}
