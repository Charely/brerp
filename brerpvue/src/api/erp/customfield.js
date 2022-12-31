import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'

export const queryscmobjectlist=()=>getAction("/base/scmobject/objecttree");