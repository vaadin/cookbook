import "../recipe/geo-location/geo-location.js";
import Tag from "../generated/com/vaadin/recipes/recipe/Tag.js";
import type { ViewConfig } from "./_shared/ViewConfig.js";
export const config: ViewConfig = {
    title: "Get the position (geo location) of the user",
    detail: {
        description: "Show how you watch the geo location of the user and keep track of the coordinates, altitude, direction and speed.",
        tags: [Tag.TYPE_SCRIPT, Tag.HILLA]
    },
};
export default function GeoLocation() {
    return <geo-location />;
}
