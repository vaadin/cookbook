export const getSimpleName = (fullName: string) => {
  return fullName.substring(fullName.lastIndexOf("/") + 1);
};
