export const getSimpleName = (fullName: string) => {
  return fullName.substring(fullName.lastIndexOf("/") + 1);
};

export const replaceQueryParameter = (key:string,value:string) => {
  const params = new URLSearchParams(window.location.search);
  if (value === "") {
    params.delete(key);
  }else {
    params.set(key,value);
  }

  const newLocation =  `${window.location.origin}${window.location.pathname}?${params.toString()}`;
  history.replaceState(null, '', newLocation);
}
