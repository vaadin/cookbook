import type { DetailedHTMLProps, HTMLAttributes } from 'react';

export type IconProps = DetailedHTMLProps<HTMLAttributes<HTMLElement>, HTMLElement> &
  Readonly<{
    children: string;
  }>;

export function InlineIcon({ children, ...rest }: IconProps) {
  return <i className={`las la-${children}`} aria-hidden="true" {...rest}></i>;
}

export function Icon(props: IconProps) {
  return (
    <div className="icon-wrapper">
      <InlineIcon {...props} />
    </div>
  );
}
