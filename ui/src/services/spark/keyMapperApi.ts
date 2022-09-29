// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

const getList = async function (
  params: {
    name: string;
    type?: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/mapper/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

export async function createNew(params: any, options?: { [key: string]: any }) {
  return request<any>('/api/mapper/create_new', {
    method: 'post',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

const getAllMapperType = async function () {
  return request<API.KeyValue>('/api/mapper/get_all_key_mapper_type', {
    method: 'GET',
  });
};

const view = async function (
  params: {
    name: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/mapper/view', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

const disable = async function (
  params: {
    name: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/mapper/disable', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

export default {
  getList,
  view,
  disable,
  createNew,
  getAllMapperType,
};
